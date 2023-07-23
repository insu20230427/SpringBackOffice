package com.example.springbackoffice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login-page")
    public String loginPage() {
        return "login"; // index.html 파일명 (확장자 제외)
    }

    @GetMapping("/signup-page")
    public String signupPage() {
        return "signup"; // index.html 파일명 (확장자 제외)
    }

    @GetMapping("/backoffice")
    @PreAuthorize("hasRole('ROLE_ADMIN')")

    public String backoffice() {
        return "backoffice";
    }
}