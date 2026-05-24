package com.ylc.progress_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    @GetMapping("/report/input")
    public String showReportInputForm(Model model) {
        // 一旦、仮の名前を返しておきます（これがあるだけで迷子URLが解消されます）
        return "report_input";
    }
}