package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.entity.Curriculum;
import com.ylc.progress_management_system.repository.CurriculumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/master/curriculum")
public class MasterCurriculumController {

    @Autowired
    private CurriculumRepository curriculumRepository;

    // 入力画面 兼 一覧画面の表示
    @GetMapping
    public String index(Model model) {
        List<Curriculum> curriculums = curriculumRepository.findAll();
        model.addAttribute("curriculums", curriculums);
        model.addAttribute("curriculum", new Curriculum());

        // ★ これを追加：登録済みの教材名リストを渡す
        model.addAttribute("existingTextbooks", curriculumRepository.findDistinctTextbookNames());

        return "master/curriculum";
    }
    // 登録処理
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Curriculum curriculum,
                      BindingResult result,
                      @RequestParam(value = "isEdit", defaultValue = "false") boolean isEdit,
                      Model model) {

        // 新規登録モードなのに、同じIDが既にDBにある場合
        if (!isEdit && curriculumRepository.existsById(curriculum.getId())) {
            result.rejectValue("id", "error.id", "この管理IDは既に登録されています。修正ボタンから編集するか、別のIDを入力してください。");
        }

        if (result.hasErrors()) {
            model.addAttribute("curriculums", curriculumRepository.findAll());
            model.addAttribute("existingTextbooks", curriculumRepository.findDistinctTextbookNames());
            model.addAttribute("isEdit", isEdit); // 状態を維持
            return "master/curriculum";
        }

        curriculumRepository.save(curriculum);
        return "redirect:/master/curriculum";
    }
    //編集処理
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        Curriculum curriculum = curriculumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid curriculum Id:" + id));

        model.addAttribute("curriculum", curriculum); // フォームに既存データをセット
        model.addAttribute("curriculums", curriculumRepository.findAll());
        model.addAttribute("existingTextbooks", curriculumRepository.findDistinctTextbookNames());
        model.addAttribute("isEdit", true); // 編集モードであることを知らせるフラグ

        return "master/curriculum";
    }
    //削除処理
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        curriculumRepository.deleteById(id);
        return "redirect:/master/curriculum";
    }
}