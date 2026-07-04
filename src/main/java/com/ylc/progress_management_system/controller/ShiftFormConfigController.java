package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.entity.ShiftFormConfig;
import com.ylc.progress_management_system.entity.ShiftFormConfigDate;
import com.ylc.progress_management_system.service.ShiftFormConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shift-form-configs")
@RequiredArgsConstructor
public class ShiftFormConfigController {

    private final ShiftFormConfigService shiftFormConfigService;

    // シフトフォーム設定の一覧表示
    @GetMapping
    public String listShiftFormConfigs(Model model) {
        List<ShiftFormConfig> configs = shiftFormConfigService.getAllShiftFormConfigs();
        model.addAttribute("configs", configs);
        return "shift_form_configs/list";
    }

    // 新しいシフトフォーム設定の作成フォーム表示
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        ShiftFormConfig config = shiftFormConfigService.generateInitialShiftFormConfig(YearMonth.now(), "新しいシフト希望フォーム");
        model.addAttribute("config", config);
        model.addAttribute("yearMonth", YearMonth.now());
        return "shift_form_configs/form";
    }

    // シフトフォーム設定の編集フォーム表示
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ShiftFormConfig> configOptional = shiftFormConfigService.getShiftFormConfigById(id);
        if (configOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "指定されたシフトフォーム設定が見つかりませんでした。");
            return "redirect:/shift-form-configs";
        }
        ShiftFormConfig config = configOptional.get();
        // configDatesがnullの場合に空のリストをセット
        if (config.getConfigDates() == null) {
            config.setConfigDates(new ArrayList<>());
        }
        model.addAttribute("config", config);
        model.addAttribute("yearMonth", YearMonth.from(config.getTargetMonth()));
        return "shift_form_configs/form";
    }

    // シフトフォーム設定の保存
    @PostMapping("/save")
    public String saveShiftFormConfig(@ModelAttribute ShiftFormConfig config, RedirectAttributes redirectAttributes) {
        // configDatesがnullの場合に空のリストをセット（フォームから送信されない場合があるため）
        if (config.getConfigDates() == null) {
            config.setConfigDates(new ArrayList<>());
        }
        // configDates内のconfigSlotsも同様に処理
        for (ShiftFormConfigDate configDate : config.getConfigDates()) {
            if (configDate.getConfigSlots() == null) {
                configDate.setConfigSlots(new ArrayList<>());
            }
        }

        ShiftFormConfig savedConfig = shiftFormConfigService.saveShiftFormConfig(config);

        // 公開フラグがtrueの場合、publicUrlを生成
        if (savedConfig.getPublishFlag()) {
            savedConfig.setPublicUrl("/mentor-shifts/request/" + savedConfig.getId());
            shiftFormConfigService.saveShiftFormConfig(savedConfig); // URLを更新するために再度保存
        } else {
            // 非公開の場合、publicUrlをクリア
            if (savedConfig.getPublicUrl() != null) {
                savedConfig.setPublicUrl(null);
                shiftFormConfigService.saveShiftFormConfig(savedConfig); // URLを更新するために再度保存
            }
        }

        redirectAttributes.addFlashAttribute("successMessage", "シフトフォーム設定が保存されました。");
        return "redirect:/shift-form-configs";
    }

    // シフトフォーム設定の公開/非公開切り替え
    @PostMapping("/toggle-publish/{id}")
    public String togglePublish(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<ShiftFormConfig> configOptional = shiftFormConfigService.getShiftFormConfigById(id);
        if (configOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "指定されたシフトフォーム設定が見つかりませんでした。");
            return "redirect:/shift-form-configs";
        }
        ShiftFormConfig config = configOptional.get();
        config.setPublishFlag(!config.getPublishFlag()); // フラグを反転

        // 公開URLを生成/クリア
        if (config.getPublishFlag()) {
            config.setPublicUrl("/mentor-shifts/request/" + config.getId()); // IDが確定しているためここで生成
        } else {
            config.setPublicUrl(null);
        }
        shiftFormConfigService.saveShiftFormConfig(config);
        redirectAttributes.addFlashAttribute("successMessage", "シフトフォーム設定の公開状態が変更されました。");
        return "redirect:/shift-form-configs";
    }

    // シフトフォーム設定の削除
    @PostMapping("/delete/{id}")
    public String deleteShiftFormConfig(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        shiftFormConfigService.deleteShiftFormConfig(id);
        redirectAttributes.addFlashAttribute("successMessage", "シフトフォーム設定が削除されました。");
        return "redirect:/shift-form-configs";
    }

    // Ajaxリクエストで日付ブロックを生成するためのエンドポイント
    @GetMapping("/generate-date-block")
    public String generateDateBlock(@RequestParam int index, Model model) {
        ShiftFormConfigDate newConfigDate = new ShiftFormConfigDate();
        newConfigDate.setConfigDate(LocalDate.now()); // デフォルトで今日の日付を設定
        newConfigDate.setConfigSlots(shiftFormConfigService.generateDefaultConfigSlots(newConfigDate));

        model.addAttribute("configDate", newConfigDate);
        model.addAttribute("dateIndex", index);
        return "shift_form_configs/fragments :: dateBlock"; // フラグメントを返す
    }
}