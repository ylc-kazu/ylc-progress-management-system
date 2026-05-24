package com.ylc.progress_management_system.controller;

import com.ylc.progress_management_system.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // 💡 1. 予約CSVアップロード画面の表示
    @GetMapping("/reservations/new")
    public String showUploadForm() {
        return "reservations/new";
    }

    // 💡 2. CSVアップロード処理の実行
    @PostMapping("/reservations/import")
    public String importCsv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "ファイルを選択してください。");
            return "redirect:/reservations/new";
        }

        try {
            // 💡 修正：将来ログイン機能と連動させる布石として、一旦「大橋校(OHASHI)」を定義します
            String currentLoginUserClassroom = "OHASHI";

            // 💡 修正：第2引数に教室コードを渡すように連動させました！
            reservationService.importCsv(file, currentLoginUserClassroom);

            redirectAttributes.addFlashAttribute("successMessage", "予約情報のCSV一括取り込みが成功しました！");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
        }

        return "redirect:/reservations/new";
    }

    // 💡 3. 予約情報一覧画面の表示
    @GetMapping("/reservations")
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservations/list";
    }
}