package com.ylc.progress_management_system.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoggingAuthFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String password = super.obtainPassword(request);
        System.out.println("【DEBUG】受け取った password → [" + password + "]");
        return password;
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username = super.obtainUsername(request);
        System.out.println("【DEBUG】受け取った username → [" + username + "]");
        return username;
    }
}