package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.dto.StudentFullForm;
import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentContact;
import com.ylc.progress_management_system.service.StudentImportService;
import com.ylc.progress_management_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // これが1つだけあればOK

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
            Student student = form.getStudent();
            // 手入力なので MANUAL を設定
            student.setRegistrationSource("MANUAL");
            // 区分を設定（手入力なので MANUAL）
            if (student.getRegistrationSource() == null) {
                student.setRegistrationSource("MANUAL");
            }

            // プロフィール紐付け
            if (form.getProfile() != null) {
                form.getProfile().setStudent(student);
                student.setStudentProfile(form.getProfile());
            }

// 連絡先紐付け
            if (form.getContacts() != null) {
                // 型を明示的に指定して、Javaの混乱を防ぐ
                List<StudentContact> validContacts = form.getContacts().stream()
                        .filter(c -> c.getName() != null && !c.getName().isBlank())
                        .peek(c -> c.setStudent(student))
                        .collect(Collectors.toList());
                student.setContacts(validContacts);
            }

            studentService.saveStudent(student);
            return "redirect:/students/new";
        } catch (Exception e) {
            // 4. エラー時の対応：一覧データが消えないように、再取得してModelに入れる
            System.err.println("保存エラー: " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("form", form);
            model.addAttribute("studentList", studentService.getAllStudents()); // これがないと一覧が消える
            model.addAttribute("errorMessage", "保存に失敗しました: " + e.getMessage());
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