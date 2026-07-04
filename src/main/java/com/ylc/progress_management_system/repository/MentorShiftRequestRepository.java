package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.MentorShiftRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MentorShiftRequestRepository extends JpaRepository<MentorShiftRequest, Long> {

    // 特定のメンターと対象月のシフト希望を取得
    Optional<MentorShiftRequest> findByMentorCodeAndRequestMonth(String mentorCode, LocalDate requestMonth);

    // 特定のメンターとシフトフォーム設定IDでシフト希望を取得
    Optional<MentorShiftRequest> findByMentorCodeAndShiftFormConfigId(String mentorCode, Long shiftFormConfigId);
}
