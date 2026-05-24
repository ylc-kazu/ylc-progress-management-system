package com.ylc.progress_management_system.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            // 💡 ログインユーザーの名前（adminなど）を "loginName" という名前でお皿に載せる
            model.addAttribute("loginName", userDetails.getUsername());
        } else {
            model.addAttribute("loginName", "ゲスト");
        }
        return "home";
    }
}