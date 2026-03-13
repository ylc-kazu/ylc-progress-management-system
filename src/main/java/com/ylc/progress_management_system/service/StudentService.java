package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentProfile;
import com.ylc.progress_management_system.entity.StudentContact;
import com.ylc.progress_management_system.repository.StudentRepository;
import com.ylc.progress_management_system.repository.StudentProfileRepository;
import com.ylc.progress_management_system.repository.StudentContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentProfileRepository profileRepository;
    private final StudentContactRepository contactRepository;

    // 生徒一覧を取得
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 生徒をIDで取得
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // 生徒基本情報を保存
    @Transactional
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // プロフィールを保存
    @Transactional
    public void saveProfile(StudentProfile profile) {
        profileRepository.save(profile);
    }

    // 連絡先を保存
    @Transactional
    public void saveContact(StudentContact contact) {
        contactRepository.save(contact);
    }

    // 生徒IDからプロフィールを取得
    public StudentProfile getProfileByStudentId(Long studentId) {
        return profileRepository.findByStudentId(studentId);
    }

    // StudentService.java の59行目付近を以下のように修正
    public List<StudentContact> getContactsByStudentId(Long studentId) {
        // 存在しない priorityAsc(studentId) を削除し、一旦空のリストを返すようにします
        return java.util.Collections.emptyList();
    }
}