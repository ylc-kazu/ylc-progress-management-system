package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentContact;
import com.ylc.progress_management_system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // StudentImportServiceを注入
    @Autowired
    private StudentImportService studentImportService;


    // 💡 修正：第2引数に「String loginClassroomCode」をしっかり定義しました
    @Transactional
    public void importCsv(MultipartFile file, String loginClassroomCode) throws Exception {
        // CSVインポートのロジックをStudentImportServiceに委譲
        studentImportService.importStudentsCsv(file.getInputStream(), loginClassroomCode);
    }

    private StudentContact createContact(Student student, String classroomCode, String relation, String name, String phone, String email, int priority) {
        StudentContact c = new StudentContact();
        c.setStudent(student);
        c.setClassroomCode(classroomCode);
        c.setRelation(relation);
        c.setName(name);
        c.setPhone(phone);
        c.setEmail(email);
        c.setPriority(priority);
        return c;
    }

    // cleanValueとgetSafeValueはStudentImportServiceに移動したため、ここでは不要ですが、
    // もし他の場所で利用されている場合は残してください。
    // 今回の変更ではStudentServiceのimportCsvからのみ呼び出されていたため、削除します。
    /*
    private String cleanValue(String val) {
        if (val == null) return "";
        String trimmed = val.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    private String getSafeValue(String[] data, int index) {
        if (data == null || index < 0 || index >= data.length) return "";
        return data[index];
    }
    */

    public List<Student> getStudentsByClassroom(String classroomCode, String role) {
        if ("ADMIN".equals(role)) {
            return studentRepository.findAll();
        }
        return studentRepository.findAll();
    }

    // 生徒コードで生徒を取得するメソッドを追加
    public Optional<Student> getStudentByStudentCode(String studentCode) {
        return studentRepository.findById(studentCode);
    }
}
