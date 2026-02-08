package com.ylc.progress_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // ログインページは誰でもアクセス可能
                        .anyRequest().authenticated()          // それ以外はログイン必須
                )
                .formLogin(form -> form
                        .loginPage("/login")                  // 自作ログインページ
                        .defaultSuccessUrl("/home")           // ログイン成功後の遷移先
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();

    }
}