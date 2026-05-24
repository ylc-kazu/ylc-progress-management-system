package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    // 💡 生徒コードがPKになったため、標準の findById() が自動的に生徒コード検索になります。
}