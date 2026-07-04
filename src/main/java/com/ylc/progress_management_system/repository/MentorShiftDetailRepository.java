package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.MentorShiftDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorShiftDetailRepository extends JpaRepository<MentorShiftDetail, Long> {
    // 必要に応じてカスタムクエリを追加
}
