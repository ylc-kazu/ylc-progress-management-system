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
    public String add(@Valid @ModelAttribute Curriculum curriculum, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // エラー内容をコンソールで確認できるようにする（デバッグ用）
            result.getAllErrors().forEach(err -> System.out.println("Validation Error: " + err.getDefaultMessage()));

            model.addAttribute("curriculums", curriculumRepository.findAll());
            model.addAttribute("existingTextbooks", curriculumRepository.findDistinctTextbookNames());
            return "master/curriculum"; // 保存せずに画面に戻る
        }
        curriculumRepository.save(curriculum);
        return "redirect:/master/curriculum";
    }
    //削除処理
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        curriculumRepository.deleteById(id);
        return "redirect:/master/curriculum";
    }
}