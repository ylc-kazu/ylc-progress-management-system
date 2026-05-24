package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.repository.StudentRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class StudentImportService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public void importCsv(InputStream is) throws Exception {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        // Swimmy本部のCSV形式に合わせる（ヘッダーがある前提）
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        Iterable<CSVRecord> csvRecords = csvParser.getRecords();

        for (CSVRecord record : csvRecords) {
            String studentCode = record.get("生徒コード");
            // 💡 修正箇所：生徒コードが主キーになったので、findByStudentCode ではなく、標準の findById を使います
            Optional<Student> existingStudent = studentRepository.findById(studentCode);

            if (existingStudent.isPresent()) {
                Student s = existingStudent.get();
                // ★ 手入力(MANUAL)データの場合はスキップして上書きを防ぐ
                if ("MANUAL".equals(s.getRegistrationSource())) {
                    continue;
                }
                // IMPORTデータの場合は情報を更新（例: 名前、在籍状況など）
                updateStudentFromCsv(s, record);
                studentRepository.save(s);
            } else {
                // 新規データは IMPORT 区分で作成
                Student newStudent = new Student();
                newStudent.setStudentCode(studentCode);
                newStudent.setRegistrationSource("IMPORT");
                updateStudentFromCsv(newStudent, record);
                studentRepository.save(newStudent);
            }
        }
    }

    private void updateStudentFromCsv(Student student, CSVRecord record) {
        student.setName(record.get("名前"));
        student.setStatus(record.get("在籍状況"));
        // 他にCSVから取り込みたい項目があればここに追加
    }
}