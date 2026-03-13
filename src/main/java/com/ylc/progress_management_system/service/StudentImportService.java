package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentProfile;
import com.ylc.progress_management_system.repository.StudentRepository;
import com.ylc.progress_management_system.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@Service
@RequiredArgsConstructor
public class StudentImportService {

    private final StudentRepository studentRepository;
    private final StudentProfileRepository profileRepository;

    @Transactional
    public void importCsv(InputStream inputStream) throws Exception {
        // 標準機能(BufferedReader)で一行ずつ読み込みます
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("MS932")))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; } // 1行目（ヘッダー）を飛ばす

                // カンマで分割（簡易版）
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                // CSVの列番号に合わせてデータを抽出（例：0番目が生徒コード、1番目が生徒名...）
                if (data.length < 2) continue;

                String studentCode = data[0].replace("\"", ""); // 引用符を除去

                // 生徒情報の登録・更新
                Student student = studentRepository.findByStudentCode(studentCode).orElse(new Student());
                student.setStudentCode(studentCode);
                student.setName(data[1].replace("\"", ""));
                studentRepository.save(student);

                // プロフィール情報の登録・更新
                StudentProfile profile = profileRepository.findByStudent(student).orElse(new StudentProfile());
                profile.setStudent(student);
                // 必要に応じて data[2], data[3]... を各フィールドにセット
                profileRepository.save(profile);
            }
        }
    }
}