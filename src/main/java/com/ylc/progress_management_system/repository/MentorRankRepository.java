package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.MentorRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MentorRankRepository extends JpaRepository<MentorRank, Long> {
    // 表示順に並べて取得
    List<MentorRank> findAllByOrderByDisplayOrderAsc();
}