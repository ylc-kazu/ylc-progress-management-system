package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    // 生徒IDからプロフィールを取得（1対1なので Optional で返す）
    StudentProfile findByStudentId(Long studentId);
}