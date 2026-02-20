package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.dto.StudentFullForm;
import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.entity.StudentProfile;
import com.ylc.progress_management_system.entity.StudentContact;
import com.ylc.progress_management_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    // 生徒一覧
    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "students/list";  // students/list.html を表示
    }

    // 生徒詳細（プロフィール＋連絡先）
    @GetMapping("/{id}")
    public String studentDetail(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id).orElse(null);
        StudentProfile profile = studentService.getProfileByStudentId(id);
        List<StudentContact> contacts = studentService.getContactsByStudentId(id);

        model.addAttribute("student", student);
        model.addAttribute("profile", profile);
        model.addAttribute("contacts", contacts);

        return "students/detail";  // students/detail.html を表示
    }

    // プロフィール編集画面
    @GetMapping("/{id}/profile/edit")
    public String editProfile(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id).orElse(null);
        StudentProfile profile = studentService.getProfileByStudentId(id);

        // プロフィールが無ければ新規作成用の空オブジェクトを渡す
        if (profile == null) {
            profile = new StudentProfile();
            profile.setStudent(student);
        }

        model.addAttribute("student", student);
        model.addAttribute("profile", profile);

        return "students/profile_edit";  // students/profile_edit.html
    }

    // プロフィール保存
    @PostMapping("/{id}/profile/save")
    public String saveProfile(
            @PathVariable Long id,
            @ModelAttribute StudentProfile profile
    ) {
        Student student = studentService.getStudentById(id).orElse(null);
        profile.setStudent(student);
        studentService.saveProfile(profile);

        return "redirect:/students/" + id;
    }

    // 連絡先追加画面
    @GetMapping("/{id}/contacts/new")
    public String newContact(@PathVariable Long id, Model model) {
        StudentContact contact = new StudentContact();
        Student student = studentService.getStudentById(id).orElse(null);
        contact.setStudent(student);

        model.addAttribute("contact", contact);
        model.addAttribute("student", student);

        return "students/contact_new";  // students/contact_new.html
    }

    // 連絡先保存
    @PostMapping("/{id}/contacts/save")
    public String saveContact(
            @PathVariable Long id,
            @ModelAttribute StudentContact contact
    ) {
        Student student = studentService.getStudentById(id).orElse(null);
        contact.setStudent(student);
        studentService.saveContact(contact);

        return "redirect:/students/" + id;
    }

    // 連絡先削除
    @PostMapping("/{studentId}/contacts/{contactId}/delete")
    public String deleteContact(
            @PathVariable Long studentId,
            @PathVariable Long contactId
    ) {
        studentService.deleteContact(contactId);
        return "redirect:/students/" + studentId;
    }

    // 生徒新規登録画面（タブ式）
    @GetMapping("/new")
    public String newStudent(Model model) {

        // StudentFullForm を作成
        StudentFullForm form = new StudentFullForm();

        // 連絡先を1件だけ初期表示（th:each のため）
        form.getContacts().add(new StudentContact());

        // new.html の th:object="${form}" に対応
        model.addAttribute("form", form);

        return "students/new";
    }

    @PostMapping("/full-save")
    public String saveFullStudent(@ModelAttribute("form") StudentFullForm form) {

        // 1. 生徒を保存
        Student savedStudent = studentService.saveStudent(form.getStudent());

        // 2. プロフィールを保存（生徒と紐付け）
        StudentProfile profile = form.getProfile();
        profile.setStudent(savedStudent);
        studentService.saveProfile(profile);

        // 3. 連絡先を保存（複数）
        for (StudentContact contact : form.getContacts()) {
            // 空欄の連絡先はスキップ
            if (contact.getName() == null || contact.getName().isBlank()) {
                continue;
            }
            contact.setStudent(savedStudent);
            studentService.saveContact(contact);
        }

        return "redirect:/students";
    }

}
