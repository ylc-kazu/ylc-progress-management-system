package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // ★この1行を追加：生徒コードで検索できるようにします
    Optional<Student> findByStudentCode(String studentCode);
}