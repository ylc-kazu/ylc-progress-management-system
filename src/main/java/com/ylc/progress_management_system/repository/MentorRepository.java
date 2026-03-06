package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    // 割り振り時に使用：現在アクティブなメンターのみ取得
    List<Mentor> findByActiveTrue();
}