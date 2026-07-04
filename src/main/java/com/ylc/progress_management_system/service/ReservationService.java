package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Mentor;
import com.ylc.progress_management_system.entity.Reservation;
import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.repository.MentorRepository;
import com.ylc.progress_management_system.repository.ReservationRepository;
import com.ylc.progress_management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository; // MentorRepositoryを注入

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/[M][MM]/[d][dd]");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("[H][HH]:[m][mm]");

    @Transactional(rollbackFor = Exception.class)
    public void importCsv(MultipartFile file, String loginClassroomCode) throws Exception {

        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "MS932"));

        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());

        List<CSVRecord> csvRecords = csvParser.getRecords();
        if (csvRecords.isEmpty()) return;

        // 予約CSVかどうかの判定（必須項目が存在するか）
        // 新しいCSVヘッダーに合わせて修正
        if (!csvRecords.get(0).isMapped("開講日") || !csvRecords.get(0).isMapped("生徒コード")) {
            throw new IllegalArgumentException("【エラー】このファイルは予約情報CSVではないようです。必須項目（開講日、生徒コード）が見つかりません。");
        }

        List<Reservation> reservationsToSave = new ArrayList<>();

        for (CSVRecord record : csvRecords) {
            try {
                // 新しいCSVヘッダーから生徒コードと生徒名を取得
                String studentCode = record.get("生徒コード");
                String studentName = record.get("生徒"); // 新しいCSVの「生徒」カラム

                String lessonDateStr = record.get("開講日");
                String startTimeStr = record.get("開講開始時刻");

                if (studentCode == null || lessonDateStr == null || startTimeStr == null ||
                        studentCode.trim().isEmpty() || lessonDateStr.trim().isEmpty() || startTimeStr.trim().isEmpty()) {
                    log.warn("生徒コード、開講日、または開講開始時刻が空のレコードをスキップします: {}", record.toString());
                    continue;
                }

                // 1. 生徒存在チェック
                Student student = studentRepository.findById(studentCode.trim())
                        .orElseThrow(() -> new IllegalArgumentException(
                                String.format("行%d: 生徒コード[%s]がマスターに未登録です。", record.getRecordNumber(), studentCode)));

                // 2. 日付・時間変換
                LocalDate lessonDate = LocalDate.parse(lessonDateStr.trim(), dateFormatter);
                LocalTime startTime = LocalTime.parse(startTimeStr.trim(), timeFormatter);

                // 3. 予約の検索と作成
                Optional<Reservation> existingOpt = reservationRepository
                        .findByClassroomCodeAndStudentCodeAndLessonDateAndStartTime(loginClassroomCode, studentCode, lessonDate, startTime);

                Reservation reservation = existingOpt.orElseGet(() -> {
                    Reservation res = new Reservation();
                    res.setClassroomCode(loginClassroomCode);
                    res.setStudentCode(studentCode);
                    res.setLessonDate(lessonDate);
                    res.setStartTime(startTime);
                    res.setIsCompleted(false);
                    // 新規作成時は出欠状況などを初期化
                    res.setAttendanceStatus("未定");
                    res.setIsLate(false);
                    res.setIsEarlyLeave(false);
                    res.setRemarks("");
                    return res;
                });

                // 4. データ更新
                reservation.setStudent(student); // 💡 エンティティの紐付け
                reservation.setCampus(record.isMapped("校舎") ? record.get("校舎") : null);
                reservation.setClassroom(record.isMapped("教室") ? record.get("教室") : null);
                // 新しいCSVにはクラスコード、クラス名がないため、既存の値を保持するかnull
                // reservation.setClassCode(record.get("クラスコード"));
                // reservation.setClassName(record.get("クラス名"));

                if (record.isMapped("開講終了時刻") && !record.get("開講終了時刻").isEmpty()) {
                    reservation.setEndTime(LocalTime.parse(record.get("開講終了時刻").trim(), timeFormatter));
                } else {
                    reservation.setEndTime(null); // 終了時刻がない場合
                }

                // コースコードとコース名も新しいCSVヘッダーに合わせる
                reservation.setCourseCode(record.isMapped("契約条件コースコード") ? record.get("契約条件コースコード") : null);
                reservation.setCourseName(record.isMapped("契約条件コース名") ? record.get("契約条件コース名") : null);
                reservation.setLesson(record.isMapped("レッスン") ? record.get("レッスン") : null);
                
                // 講師情報のマッピング
                String mentorNameFromCsv = record.isMapped("講師") ? record.get("講師") : null;
                if (mentorNameFromCsv != null && !mentorNameFromCsv.trim().isEmpty()) {
                    // 講師名からメンターコードを解決（完全一致で検索）
                    Optional<Mentor> mentorOpt = mentorRepository.findByName(mentorNameFromCsv.trim());
                    if (mentorOpt.isPresent()) {
                        reservation.setMentorCode(mentorOpt.get().getMentorCode());
                        reservation.setMentor(mentorOpt.get());
                    } else {
                        log.warn("行{}: 講師名 '{}' に対応するメンターが見つかりませんでした。", record.getRecordNumber(), mentorNameFromCsv);
                        reservation.setMentorCode(null);
                        reservation.setMentor(null);
                    }
                } else {
                    reservation.setMentorCode(null);
                    reservation.setMentor(null);
                }
                
                // teacherフィールドはmentorCodeと重複するため、mentorCodeを使用
                reservation.setTeacher(mentorNameFromCsv); // 講師名をteacherフィールドにも設定（互換性のため）
                reservation.setStudentName(studentName);

                reservationsToSave.add(reservation);

            } catch (Exception e) {
                log.error("CSVインポート中にエラー発生 (行: {}): {}", record.getRecordNumber(), e.getMessage());
                throw new Exception("インポートエラー (行 " + record.getRecordNumber() + "): " + e.getMessage(), e);
            }
        }

        reservationRepository.saveAll(reservationsToSave);
        log.info("予約情報CSVインポート完了: {}件", reservationsToSave.size());
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
