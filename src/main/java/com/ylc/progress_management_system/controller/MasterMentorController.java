package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.entity.Mentor;
import com.ylc.progress_management_system.entity.MentorRank;
import com.ylc.progress_management_system.repository.MentorRankRepository;
import com.ylc.progress_management_system.repository.MentorRepository;
import com.ylc.progress_management_system.repository.CurriculumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/master/mentor")
public class MasterMentorController {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private CurriculumRepository curriculumRepository;

    @Autowired
    private MentorRankRepository mentorRankRepository;

    @GetMapping
    public String index(Model model) {
        List<Mentor> mentors = mentorRepository.findAll();

        // 全てのデータに対して、NULLでないことを保証する（念のための全上書き）
        for (Mentor m : mentors) {
            if (m.getHourlyRate() == null) m.setHourlyRate(1100);
            if (m.getRank() == null) m.setRank(new MentorRank());
        }

        Mentor mentor = new Mentor();
        mentor.setRank(new MentorRank());
        mentor.setHourlyRate(1100); // 初期値

        model.addAttribute("mentor", mentor);
        model.addAttribute("mentors", mentors);
        model.addAttribute("allCurriculums", curriculumRepository.findAll());
        model.addAttribute("allRanks", mentorRankRepository.findAllByOrderByDisplayOrderAsc());
        model.addAttribute("isEdit", false);
        return "master/mentor";
    }

    @PostMapping("/add")
    public String addMentor(@ModelAttribute Mentor mentor) {
        // ランク選択が「未選択」の場合、リレーションエラーを防ぐためにnullにする
        if (mentor.getRank() != null && mentor.getRank().getId() == null) {
            mentor.setRank(null);
        }

        // hourlyRateがDBに残っているなら、保存時にエラーにならないよう値を確保
        if (mentor.getHourlyRate() == null) {
            mentor.setHourlyRate(1100);
        }

        mentorRepository.save(mentor);
        return "redirect:/master/mentor";
    }

    @GetMapping("/edit/{id}")
    public String editMentor(@PathVariable Long id, Model model) {
        Mentor mentor = mentorRepository.findById(id).orElseThrow();
        if (mentor.getRank() == null) {
            mentor.setRank(new MentorRank());
        }
        model.addAttribute("mentor", mentor);
        model.addAttribute("mentors", mentorRepository.findAll());
        model.addAttribute("allCurriculums", curriculumRepository.findAll());
        model.addAttribute("allRanks", mentorRankRepository.findAllByOrderByDisplayOrderAsc());
        model.addAttribute("isEdit", true);
        return "master/mentor";
    }

    @PostMapping("/delete/{id}")
    public String deleteMentor(@PathVariable Long id) {
        mentorRepository.deleteById(id);
        return "redirect:/master/mentor";
    }
}