package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    // 生徒IDからプロフィールを取得（1対1なので Optional で返す）
    StudentProfile findByStudentId(Long studentId);
    Optional<StudentProfile> findByStudent(Student student);
}