package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentProfile;
import com.ylc.progress_management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentImportService {

    private final StudentRepository studentRepository;

    /**
     * 本部生徒情報CSVを一括インポート（手動退会ガード ＆ ChatGPT NG自動検知版）
     */
    @Transactional
    public void importStudentsCsv(InputStream inputStream, String currentClassroomCode) throws Exception {

        // CSVファイルをUTF-8で読み込み
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        // 新しいCSVフォーマットに合わせてヘッダーを設定
        // CSVFormat.DEFAULT.withFirstRecordAsHeader() を使用し、CSVのヘッダーを自動で読み込む
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());

        List<CSVRecord> csvRecords = csvParser.getRecords();
        if (csvRecords.isEmpty()) {
            return;
        }

        // 🚀 パフォーマンス対策：CSVにある全生徒コードをDBから一括取得してMap化（全校データ対策）
        // 新しいCSVに「生徒コード」が含まれることを前提とする
        Set<String> csvStudentCodes = csvRecords.stream()
                .map(record -> record.get("生徒コード"))
                .filter(code -> code != null && !code.trim().isEmpty())
                .collect(Collectors.toSet());

        Map<String, Student> existingStudentMap = studentRepository.findAllById(csvStudentCodes).stream()
                .collect(Collectors.toMap(Student::getStudentCode, Function.identity()));

        List<Student> studentsToSave = new ArrayList<>();

        // 日付フォーマッター
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");

        for (CSVRecord record : csvRecords) {
            // 新しいCSVに「生徒コード」が含まれることを前提とする
            String studentCode = record.get("生徒コード");
            if (studentCode == null || studentCode.trim().isEmpty()) {
                log.warn("生徒コードが空のレコードをスキップします: {}", record.toString());
                continue;
            }

            // CSVから特記事項（旧：備考欄）のテキストを取得（検知用）
            String remarksGeneral = record.isMapped("特記事項") ? record.get("特記事項") : "";
            String combinedRemarks = remarksGeneral.toUpperCase(); // 大文字に統一して判定

            Student student;
            StudentProfile profile;
            boolean isExisting = existingStudentMap.containsKey(studentCode);

            if (isExisting) {
                student = existingStudentMap.get(studentCode);
                profile = student.getProfile();
                if (profile == null) {
                    profile = new StudentProfile();
                    profile.setStudentCode(studentCode);
                    profile.setStudent(student);
                    student.setProfile(profile);
                }
            } else {
                student = new Student();
                student.setStudentCode(studentCode);

                profile = new StudentProfile();
                profile.setStudentCode(studentCode);
                profile.setStudent(student);
                student.setProfile(profile);
            }

            // --- 1. 🛡️ パターンB：手動退会ガードロジック ---
            // すでに画面から「退会」や「休学」に手動設定されている既存生徒の場合、
            // CSV再インポートによって勝手に「在籍（初期状態）」に戻らないよう、現在のステータスを最優先して維持します。
            if (isExisting && ("退会".equals(student.getStatus()) || "休学".equals(student.getStatus()))) {
                // ステータスは手動管理のものをキープ（何もしない）
            } else {
                // 新しいCSVの「ステータス」をStudent.statusに設定
                String status = record.isMapped("ステータス") ? record.get("ステータス") : "在籍";
                if (combinedRemarks.contains("CHATGPT NG") || combinedRemarks.contains("CHATGPTNG")) {
                    student.setStatus("ChatGPT NG");
                } else {
                    student.setStatus(status);
                }
            }

            // --- 2. Student（親）のCSV上書き項目 ---
            student.setClassroomCode(currentClassroomCode);
            student.setName(record.isMapped("お子様氏名") ? record.get("お子様氏名") : student.getName());
            student.setFurigana(record.isMapped("フリガナ") ? record.get("フリガナ") : student.getFurigana());
            // registrationSourceはCSVにないので、既存のロジックを維持
            if (combinedRemarks.contains("折込チラシ") || combinedRemarks.contains("チラシ")) {
                student.setRegistrationSource("折込チラシ");
            } else if (!isExisting) { // 新規生徒の場合のみデフォルトを設定
                student.setRegistrationSource("CSVインポート");
            }


            // --- 3. StudentProfile（子）のCSV上書き項目 ---
            // 新しいCSVに存在する項目のみを更新し、存在しない項目は既存の値を保持
            profile.setGradeText(record.isMapped("学年") ? record.get("学年") : profile.getGradeText());
            profile.setParentName(record.isMapped("保護者氏名") ? record.get("保護者氏名") : profile.getParentName());
            profile.setPhoneMobile(record.isMapped("電話番号") ? record.get("電話番号") : profile.getPhoneMobile());
            profile.setEmail1(record.isMapped("メールアドレス") ? record.get("メールアドレス") : profile.getEmail1());
            profile.setGenderText(record.isMapped("性別") ? record.get("性別") : profile.getGenderText());
            profile.setRemarksGeneral(remarksGeneral); // 特記事項をremarksGeneralにマッピング
            profile.setProgressStatus(record.isMapped("進捗") ? record.get("進捗") : profile.getProgressStatus());

            // 新しく追加される項目
            // 生年月日
            if (record.isMapped("生年月日")) {
                try {
                    profile.setBirthDate(LocalDate.parse(record.get("生年月日").trim(), dateFormatter));
                } catch (DateTimeParseException e) {
                    log.warn("生徒コード {} の生年月日 '{}' のパースに失敗しました: {}", studentCode, record.get("生年月日"), e.getMessage());
                    // パース失敗時は既存の値を保持するか、nullを設定
                    if (!isExisting) profile.setBirthDate(null);
                }
            } else if (!isExisting) {
                profile.setBirthDate(null);
            }

            // 月謝
            if (record.isMapped("月謝")) {
                try {
                    profile.setMonthlyFee(Integer.parseInt(record.get("月謝").trim()));
                } catch (NumberFormatException e) {
                    log.warn("生徒コード {} の月謝 '{}' のパースに失敗しました: {}", studentCode, record.get("月謝"), e.getMessage());
                    if (!isExisting) profile.setMonthlyFee(null);
                }
            } else if (!isExisting) {
                profile.setMonthlyFee(null);
            }

            // 契約日
            if (record.isMapped("契約日")) {
                try {
                    profile.setContractDate(LocalDate.parse(record.get("契約日").trim(), dateFormatter));
                } catch (DateTimeParseException e) {
                    log.warn("生徒コード {} の契約日 '{}' のパースに失敗しました: {}", studentCode, record.get("契約日"), e.getMessage());
                    if (!isExisting) profile.setContractDate(null);
                }
            } else if (!isExisting) {
                profile.setContractDate(null);
            }

            // コース区分
            profile.setCourseType(record.isMapped("コース区分") ? record.get("コース区分") : profile.getCourseType());
            // 受講曜日
            profile.setLessonDay(record.isMapped("受講曜日") ? record.get("受講曜日") : profile.getLessonDay());
            // 受講時間枠
            profile.setLessonSlot(record.isMapped("受講時間枠") ? record.get("受講時間枠") : profile.getLessonSlot());


            // ⚠️ 重要：手入力拡張項目（managementId, nickname, postalCode, address1, address2, schoolName, phoneHome, email2, parentNameKana, relationText, emergencyPhone, parentEmail1, parentEmail2, targetCampus, staffCode, staffName, typingTest, programmingTest, remarksText, currentText, currentChapter, currentPage）は、
            // 新しいCSVに存在しないため、既存のデータが何回インポートしても100%残ります。
            // 新規生徒の場合はnullが設定されます。

            studentsToSave.add(student);
        }

        // 4. 一括保存（バルクインサート / アップデート）
        studentRepository.saveAll(studentsToSave);
        log.info("生徒情報CSVインポートが完了しました。総件数: {}件", studentsToSave.size());
    }
}
