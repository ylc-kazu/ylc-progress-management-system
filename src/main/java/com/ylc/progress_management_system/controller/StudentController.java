package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getStudentsByClassroom("ALL", "ADMIN"));
        // 💡 修正：正しく「studentsフォルダの中のlist.html」を指定します
        return "students/list";
    }

    @PostMapping("/students/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "ファイルを選択してください。");
            return "redirect:/students";
        }

        try {
            String currentLoginUserClassroom = "OHASHI";
            studentService.importCsv(file, currentLoginUserClassroom);
            redirectAttributes.addFlashAttribute("successMessage", "生徒情報の取り込みが成功しました。");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "取り込みに失敗しました: " + e.getMessage());
        }

        return "redirect:/students";
    }
}