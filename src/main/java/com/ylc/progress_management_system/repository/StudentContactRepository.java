package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.StudentContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentContactRepository extends JpaRepository<StudentContact, Long> {

    // 生徒IDに紐づく連絡先一覧を取得
    List<StudentContact> findByStudentId(Long studentId);
}