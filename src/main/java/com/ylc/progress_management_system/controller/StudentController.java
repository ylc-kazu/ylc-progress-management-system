package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // PathVariableをインポート
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional; // Optionalをインポート

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getStudentsByClassroom("ALL", "ADMIN"));
        return "students/list";
    }

    // 生徒詳細表示
    @GetMapping("/students/{studentCode}")
    public String showStudentDetail(@PathVariable String studentCode, Model model, RedirectAttributes redirectAttributes) {
        Optional<Student> studentOptional = studentService.getStudentByStudentCode(studentCode);
        if (studentOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "生徒が見つかりませんでした。");
            return "redirect:/students";
        }
        model.addAttribute("student", studentOptional.get());
        return "students/detail"; // 新しく作成する生徒詳細ページ
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
