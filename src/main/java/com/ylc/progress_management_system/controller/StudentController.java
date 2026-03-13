package com.ylc.progress_management_system.controller;
import com.ylc.progress_management_system.service.StudentService;
import com.ylc.progress_management_system.service.StudentImportService;
import com.ylc.progress_management_system.dto.StudentFullForm;
import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentContact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentImportService importService;

    @GetMapping("/new")
    public String newStudent(Model model) {
        StudentFullForm form = new StudentFullForm();
        form.getContacts().add(new StudentContact()); // 連絡先の入力欄を1つ用意

        model.addAttribute("form", form);

        // ★ ここが重要です！
        // 画面で生徒一覧を表示している場合、必ず studentList を渡す必要があります
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("studentList", students != null ? students : new ArrayList<>());

        return "students/new";
    }

    @PostMapping("/full-save")
    public String saveFullStudent(@ModelAttribute("form") StudentFullForm form, Model model) {
        try {
            Student savedStudent = studentService.saveStudent(form.getStudent());

            if (form.getProfile() != null) {
                form.getProfile().setStudent(savedStudent);
                studentService.saveProfile(form.getProfile());
            }

            if (form.getContacts() != null) {
                for (StudentContact contact : form.getContacts()) {
                    if (contact.getName() != null && !contact.getName().isBlank()) {
                        contact.setStudent(savedStudent);
                        studentService.saveContact(contact);
                    }
                }
            }
            return "redirect:/students/new";
        } catch (Exception e) {
            model.addAttribute("form", form);
            model.addAttribute("errorMessage", "エラー: " + e.getMessage());
            return "students/new";
        }
    }

    @PostMapping("/import")
    public String importCsv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            importService.importCsv(file.getInputStream());
            redirectAttributes.addFlashAttribute("successMessage", "インポート成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "エラー: " + e.getMessage());
        }
        return "redirect:/students/new";
    }
}