package com.ylc.progress_management_system.repository;

import com.ylc.progress_management_system.entity.ShiftFormConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftFormConfigRepository extends JpaRepository<ShiftFormConfig, Long> {

    // 対象月と公開フラグでシフトフォーム設定を取得
    Optional<ShiftFormConfig> findByTargetMonthAndPublishFlag(LocalDate targetMonth, Boolean publishFlag);

    // 対象月でシフトフォーム設定を取得
    Optional<ShiftFormConfig> findByTargetMonth(LocalDate targetMonth);

    // 公開中のシフトフォーム設定を全て取得
    List<ShiftFormConfig> findByPublishFlag(Boolean publishFlag);
}
