package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.entity.Mentor;
import com.ylc.progress_management_system.entity.Student;
import com.ylc.progress_management_system.repository.MentorRepository;
import com.ylc.progress_management_system.repository.StudentRepository;
import com.ylc.progress_management_system.repository.ReservationRepository;
import com.ylc.progress_management_system.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final MentorRepository mentorRepository;       // 💡 直接利用できるように注入
    private final StudentRepository studentRepository;     // 💡 生徒ステータス取得のために注入
    private final ReservationRepository reservationRepository;

    @GetMapping("/reservations/new")
    public String showUploadForm() {
        return "reservations/new";
    }

    @PostMapping("/reservations/import")
    public String importCsv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "ファイルを選択してください。");
            return "redirect:/reservations/new";
        }

        try {
            String currentLoginUserClassroom = "OHASHI";
            reservationService.importCsv(file, currentLoginUserClassroom);
            redirectAttributes.addFlashAttribute("successMessage", "予約情報のCSV一括取り込みが成功しました！");
        } catch (IllegalArgumentException e) {
            // 💡 生徒情報が存在しないという業務エラーをキャッチして画面に優しく表示
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "システムエラーが発生しました: " + e.getMessage());
        }

        return "redirect:/reservations/new";
    }

    @GetMapping("/reservations")
    public String listReservations(Model model) {
        // 1. 全予約データを取得
        model.addAttribute("reservations", reservationService.getAllReservations());

        // 2. メンター一覧をデータベースから直接安全に全件取得（プルダウン用）
        List<Mentor> assignedMentors = mentorRepository.findAll();
        model.addAttribute("mentors", assignedMentors != null ? assignedMentors : new java.util.ArrayList<>());

        // 3. 🧠 【AI管理連携の要】全生徒のステータス（ChatGPT NG等）を高速マップ化して画面に渡す
        List<Student> allStudents = studentRepository.findAll();
        Map<String, String> studentStatusMap = new HashMap<>();
        if (allStudents != null) {
            for (Student s : allStudents) {
                studentStatusMap.put(s.getStudentCode(), s.getStatus());
            }
        }
        model.addAttribute("studentStatusMap", studentStatusMap);

        return "reservations/list";
    }

    @PostMapping("/api/reservations/assign-mentor")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<String> assignMentor(
            @RequestParam("reservationId") Long reservationId,
            @RequestParam("mentorCode") String mentorCode) {

        try {
            java.util.Optional<com.ylc.progress_management_system.entity.Reservation> opt =
                    reservationRepository.findById(reservationId);

            if (opt.isPresent()) {
                com.ylc.progress_management_system.entity.Reservation res = opt.get();
                res.setMentorCode(mentorCode.isEmpty() ? null : mentorCode);
                reservationRepository.save(res);
                return org.springframework.http.ResponseEntity.ok("Success");
            } else {
                return org.springframework.http.ResponseEntity.status(404).body("Reservation Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return org.springframework.http.ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}