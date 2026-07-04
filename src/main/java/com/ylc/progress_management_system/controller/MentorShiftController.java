package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.entity.Mentor;
import com.ylc.progress_management_system.entity.MentorShiftDetail;
import com.ylc.progress_management_system.entity.MentorShiftRequest;
import com.ylc.progress_management_system.entity.ShiftFormConfig;
import com.ylc.progress_management_system.entity.ShiftFormConfigDate;
import com.ylc.progress_management_system.entity.ShiftFormConfigSlot;
import com.ylc.progress_management_system.service.MentorService;
import com.ylc.progress_management_system.service.MentorShiftService;
import com.ylc.progress_management_system.service.ShiftFormConfigService; // 追加
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mentor-shifts")
@RequiredArgsConstructor
public class MentorShiftController {

    private final MentorShiftService mentorShiftService;
    private final MentorService mentorService;
    private final ShiftFormConfigService shiftFormConfigService; // 追加

    // シフト希望入力フォームの表示
    @GetMapping("/request/{mentorCode}/{configId}") // パスを変更
    public String showShiftRequestForm(@PathVariable String mentorCode, @PathVariable Long configId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Mentor> mentorOptional = mentorService.findByMentorCode(mentorCode);
        if (mentorOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "メンターが見つかりませんでした。");
            return "redirect:/error";
        }
        Mentor mentor = mentorOptional.get();
        model.addAttribute("mentor", mentor);

        // ShiftFormConfigServiceから設定を取得
        Optional<ShiftFormConfig> configOptional = shiftFormConfigService.getShiftFormConfigById(configId);
        if (configOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "シフトフォーム設定が見つかりませんでした。");
            return "redirect:/error";
        }
        ShiftFormConfig config = configOptional.get();
        model.addAttribute("shiftFormConfig", config); // フォーム名などを表示するために追加

        // 既存のシフト希望があれば取得
        Optional<MentorShiftRequest> existingRequest = mentorShiftService.getShiftRequestByMentorCodeAndConfigId(mentorCode, configId);
        MentorShiftRequest shiftRequest = existingRequest.orElseGet(() -> {
            MentorShiftRequest newRequest = new MentorShiftRequest();
            newRequest.setMentorCode(mentorCode);
            newRequest.setMentor(mentor);
            newRequest.setRequestMonth(config.getTargetMonth()); // 設定から対象月を取得
            newRequest.setShiftFormConfigId(configId); // 設定IDをセット
            newRequest.setShiftFormConfig(config); // リレーションもセット
            return newRequest;
        });

        // シフト詳細を生成または既存のものをセット
        List<MentorShiftDetail> shiftDetails = new ArrayList<>();
        if (existingRequest.isPresent()) {
            // 既存のシフト詳細をコピーして使用
            shiftDetails = existingRequest.get().getShiftDetails().stream()
                    .map(detail -> {
                        MentorShiftDetail newDetail = new MentorShiftDetail();
                        newDetail.setId(detail.getId());
                        newDetail.setShiftRequest(shiftRequest);
                        newDetail.setShiftDate(detail.getShiftDate());
                        newDetail.setShiftSlotName(detail.getShiftSlotName());
                        newDetail.setSlotStartTime(detail.getSlotStartTime());
                        newDetail.setSlotEndTime(detail.getSlotEndTime());
                        newDetail.setIsAvailable(detail.getIsAvailable());
                        return newDetail;
                    })
                    .collect(Collectors.toList());
        } else {
            // ShiftFormConfigの設定に基づいてシフト詳細を生成
            for (ShiftFormConfigDate configDate : config.getConfigDates()) {
                for (ShiftFormConfigSlot configSlot : configDate.getConfigSlots()) {
                    if (configSlot.getIsAvailableForRequest()) { // 希望提出可能なコマのみ
                        shiftDetails.add(createShiftDetail(shiftRequest, configDate.getConfigDate(), configSlot.getSlotName(), configSlot.getStartTime(), configSlot.getEndTime()));
                    }
                }
            }
        }
        shiftRequest.setShiftDetails(shiftDetails);
        model.addAttribute("shiftRequest", shiftRequest);
        model.addAttribute("yearMonth", YearMonth.from(config.getTargetMonth())); // 対象月を渡す

        return "mentor_shifts/form";
    }

    // シフト希望の保存
    @PostMapping("/save")
    public String saveShiftRequest(@ModelAttribute MentorShiftRequest shiftRequest, RedirectAttributes redirectAttributes) {
        // メンター情報を再取得してセット
        Optional<Mentor> mentorOptional = mentorService.findByMentorCode(shiftRequest.getMentorCode());
        mentorOptional.ifPresent(shiftRequest::setMentor);

        // ShiftFormConfig情報を再取得してセット
        Optional<ShiftFormConfig> configOptional = shiftFormConfigService.getShiftFormConfigById(shiftRequest.getShiftFormConfigId());
        configOptional.ifPresent(shiftRequest::setShiftFormConfig);

        mentorShiftService.saveShiftRequest(shiftRequest);
        redirectAttributes.addFlashAttribute("successMessage", "シフト希望が保存されました。");
        // 保存後、同じフォームに戻る
        return "redirect:/mentor-shifts/request/" + shiftRequest.getMentorCode() + "/" + shiftRequest.getShiftFormConfigId();
    }

    private MentorShiftDetail createShiftDetail(MentorShiftRequest shiftRequest, LocalDate date, String slotName, LocalTime startTime, LocalTime endTime) {
        MentorShiftDetail detail = new MentorShiftDetail();
        detail.setShiftRequest(shiftRequest);
        detail.setShiftDate(date);
        detail.setShiftSlotName(slotName);
        detail.setSlotStartTime(startTime);
        detail.setSlotEndTime(endTime);
        detail.setIsAvailable(false); // デフォルトは未選択（false）
        return detail;
    }
}
