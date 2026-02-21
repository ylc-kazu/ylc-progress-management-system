package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentProfile;
import com.ylc.progress_management_system.entity.StudentContact;
import com.ylc.progress_management_system.repository.StudentRepository;
import com.ylc.progress_management_system.repository.StudentProfileRepository;
import com.ylc.progress_management_system.repository.StudentContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final StudentContactRepository studentContactRepository;

    // 生徒一覧取得
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 生徒取得
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // 生徒保存
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // 生徒削除
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // プロフィール取得
    public StudentProfile getProfileByStudentId(Long studentId) {
        return studentProfileRepository.findByStudentId(studentId);
    }

    // プロフィール保存
    public StudentProfile saveProfile(StudentProfile profile) {
        return studentProfileRepository.save(profile);
    }

    // 連絡先一覧取得
    public List<StudentContact> getContactsByStudentId(Long studentId) {
        return studentContactRepository.findByStudentId(studentId);
    }

    // 連絡先保存
    public StudentContact saveContact(StudentContact contact) {
        return studentContactRepository.save(contact);
    }

    // 連絡先削除
    public void deleteContact(Long contactId) {
        studentContactRepository.deleteById(contactId);
    }
}