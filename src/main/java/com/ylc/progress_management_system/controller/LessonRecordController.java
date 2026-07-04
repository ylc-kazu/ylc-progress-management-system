package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.entity.LessonRecord;
import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.service.LessonRecordService;
import com.ylc.progress_management_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/lesson-records")
@RequiredArgsConstructor
public class LessonRecordController {

    private final LessonRecordService lessonRecordService;
    private final StudentService studentService; // 生徒情報を取得するために注入

    // 授業記録入力フォームの表示
    @GetMapping("/new/{studentCode}")
    public String showLessonRecordForm(@PathVariable String studentCode, Model model) {
        Optional<Student> studentOptional = studentService.getStudentByStudentCode(studentCode);
        if (studentOptional.isEmpty()) {
            // エラーハンドリング
            return "redirect:/students"; // またはエラーページ
        }
        Student student = studentOptional.get();
        model.addAttribute("student", student);
        model.addAttribute("lessonRecord", new LessonRecord());
        // 現在のメンターコードを仮で設定（認証情報から取得する想定）
        model.addAttribute("currentMentorCode", "MENTOR001");
        return "lesson_records/form"; // lesson_recordsフォルダ内のform.htmlを返す
    }

    // 授業記録の保存とAIコメント生成
    @PostMapping("/save")
    public String saveLessonRecord(@ModelAttribute LessonRecord lessonRecord, RedirectAttributes redirectAttributes) {
        // 授業時間の設定（仮）
        lessonRecord.setLessonDate(LocalDate.now());
        lessonRecord.setStartTime(LocalTime.of(10, 0)); // 例
        lessonRecord.setEndTime(LocalTime.of(11, 0));   // 例

        // AIコメント生成
        List<String> aiComments = lessonRecordService.generateAiComments(
                lessonRecord.getStudent().getName(), // 生徒名
                lessonRecord.getCurriculum(),       // カリキュラム
                lessonRecord.getMentorComment()     // メンターの感想
        );
        lessonRecord.setAiComment1(aiComments.size() > 0 ? aiComments.get(0) : null);
        lessonRecord.setAiComment2(aiComments.size() > 1 ? aiComments.get(1) : null);
        lessonRecord.setAiComment3(aiComments.size() > 2 ? aiComments.get(2) : null);

        LessonRecord savedRecord = lessonRecordService.saveLessonRecord(lessonRecord);
        redirectAttributes.addFlashAttribute("successMessage", "授業記録が保存されました。AIコメントを確認してください。");
        return "redirect:/lesson-records/edit/" + savedRecord.getId(); // AIコメント選択画面へリダイレクト
    }

    // AIコメント選択・最終コメント編集フォームの表示
    @GetMapping("/edit/{id}")
    public String editLessonRecord(@PathVariable Long id, Model model) {
        Optional<LessonRecord> lessonRecordOptional = lessonRecordService.findById(id); // findByIdはLessonRecordRepositoryに追加する必要がある
        if (lessonRecordOptional.isEmpty()) {
            return "redirect:/lesson-records"; // またはエラーページ
        }
        model.addAttribute("lessonRecord", lessonRecordOptional.get());
        return "lesson_records/edit"; // AIコメント選択・最終コメント編集用のedit.htmlを返す
    }

    // 最終コメントの保存
    @PostMapping("/finalize")
    public String finalizeLessonRecord(@ModelAttribute LessonRecord lessonRecord, RedirectAttributes redirectAttributes) {
        // 既存のレコードを取得し、最終コメントと共有日時を更新
        Optional<LessonRecord> existingRecordOptional = lessonRecordService.findById(lessonRecord.getId());
        if (existingRecordOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "授業記録が見つかりませんでした。");
            return "redirect:/lesson-records";
        }
        LessonRecord existingRecord = existingRecordOptional.get();
        existingRecord.setFinalCommentForParent(lessonRecord.getFinalCommentForParent());
        existingRecord.setSharedAt(LocalDate.now()); // 共有日時を現在に設定

        lessonRecordService.saveLessonRecord(existingRecord);
        redirectAttributes.addFlashAttribute("successMessage", "保護者共有コメントが確定されました。");
        return "redirect:/students/" + existingRecord.getStudentCode(); // 生徒詳細ページなどへリダイレクト
    }

    // 生徒ごとの授業記録一覧表示
    @GetMapping("/student/{studentCode}")
    public String listLessonRecordsForStudent(@PathVariable String studentCode, Model model) {
        Optional<Student> studentOptional = studentService.getStudentByStudentCode(studentCode);
        if (studentOptional.isEmpty()) {
            return "redirect:/students";
        }
        model.addAttribute("student", studentOptional.get());
        List<LessonRecord> lessonRecords = lessonRecordService.getLessonRecordsByStudentCode(studentCode);
        model.addAttribute("lessonRecords", lessonRecords);
        return "lesson_records/list_by_student"; // 生徒ごとの授業記録一覧表示用のlist_by_student.htmlを返す
    }
}
