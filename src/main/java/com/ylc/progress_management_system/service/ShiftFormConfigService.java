package com.ylc.progress_management_system.service;

import com.ylc.progress_management_system.entity.ShiftFormConfig;
import com.ylc.progress_management_system.entity.ShiftFormConfigDate;
import com.ylc.progress_management_system.entity.ShiftFormConfigSlot;
import com.ylc.progress_management_system.repository.ShiftFormConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShiftFormConfigService {

    private final ShiftFormConfigRepository shiftFormConfigRepository;

    // シフトフォーム設定を保存
    @Transactional
    public ShiftFormConfig saveShiftFormConfig(ShiftFormConfig config) {
        // configDatesとconfigSlotsの関連付け
        if (config.getConfigDates() != null) {
            config.getConfigDates().forEach(date -> {
                date.setShiftFormConfig(config);
                if (date.getConfigSlots() != null) {
                    date.getConfigSlots().forEach(slot -> slot.setShiftFormConfigDate(date));
                }
            });
        }
        return shiftFormConfigRepository.save(config);
    }

    // IDでシフトフォーム設定を取得
    public Optional<ShiftFormConfig> getShiftFormConfigById(Long id) {
        return shiftFormConfigRepository.findById(id);
    }

    // 対象月でシフトフォーム設定を取得
    public Optional<ShiftFormConfig> getShiftFormConfigByTargetMonth(LocalDate targetMonth) {
        return shiftFormConfigRepository.findByTargetMonth(targetMonth);
    }

    // 公開中のシフトフォーム設定を全て取得
    public List<ShiftFormConfig> getPublishedShiftFormConfigs() {
        return shiftFormConfigRepository.findByPublishFlag(true);
    }

    // 全てのシフトフォーム設定を取得
    public List<ShiftFormConfig> getAllShiftFormConfigs() {
        return shiftFormConfigRepository.findAll();
    }

    // シフトフォーム設定を削除
    @Transactional
    public void deleteShiftFormConfig(Long id) {
        shiftFormConfigRepository.deleteById(id);
    }

    /**
     * 指定された年月のシフトフォーム設定を初期生成するヘルパーメソッド
     * 初期生成時には、configDatesは空のリストとして返します。
     * @param yearMonth 対象年月
     * @param formName フォーム名
     * @return 初期生成されたShiftFormConfig
     */
    public ShiftFormConfig generateInitialShiftFormConfig(YearMonth yearMonth, String formName) {
        ShiftFormConfig config = new ShiftFormConfig();
        config.setFormName(formName);
        config.setTargetMonth(yearMonth.atDay(1));
        config.setPublishFlag(false); // 初期状態は非公開
        config.setConfigDates(new ArrayList<>()); // 空のリストをセット
        return config;
    }

    /**
     * 指定された日付のデフォルトのコマ設定を生成するヘルパーメソッド
     * @param configDate ShiftFormConfigDateオブジェクト
     * @return デフォルトのコマ設定リスト
     */
    public List<ShiftFormConfigSlot> generateDefaultConfigSlots(ShiftFormConfigDate configDate) {
        List<ShiftFormConfigSlot> configSlots = new ArrayList<>();
        // デフォルトのコマを生成 (1限〜6限 + 終日NG)
        configSlots.add(createConfigSlot(configDate, "1限(10:00〜11:00)", LocalTime.of(10, 0), LocalTime.of(11, 0)));
        configSlots.add(createConfigSlot(configDate, "2限(11:10〜12:10)", LocalTime.of(11, 10), LocalTime.of(12, 10)));
        configSlots.add(createConfigSlot(configDate, "3限(12:20〜13:20)", LocalTime.of(12, 20), LocalTime.of(13, 20))); // 3限を追加
        configSlots.add(createConfigSlot(configDate, "4限(13:30〜14:30)", LocalTime.of(13, 30), LocalTime.of(14, 30)));
        configSlots.add(createConfigSlot(configDate, "5限(14:40〜15:40)", LocalTime.of(14, 40), LocalTime.of(15, 40)));
        configSlots.add(createConfigSlot(configDate, "6限(15:50〜16:50)", LocalTime.of(15, 50), LocalTime.of(16, 50)));
        configSlots.add(createConfigSlot(configDate, "終日NG", null, null));
        return configSlots;
    }

    private ShiftFormConfigSlot createConfigSlot(ShiftFormConfigDate configDate, String slotName, LocalTime startTime, LocalTime endTime) {
        ShiftFormConfigSlot slot = new ShiftFormConfigSlot();
        slot.setShiftFormConfigDate(configDate);
        slot.setSlotName(slotName);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setIsAvailableForRequest(true); // デフォルトは全て希望可能
        return slot;
    }
}
