package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.LessonRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRecordRepository extends JpaRepository<LessonRecord, Long> {
    // 特定の生徒の授業記録を日付の降順で取得
    List<LessonRecord> findByStudentCodeOrderByLessonDateDesc(String studentCode);
}
