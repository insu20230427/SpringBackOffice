package com.example.springbackoffice.controller;

import com.example.springbackoffice.dto.AdminResponseDto;
import com.example.springbackoffice.security.UserDetailsImpl;
import com.example.springbackoffice.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AdminController {

    private final AdminService adminService;

    // 관리자 페이지 조회
    @GetMapping("/admin/page")
    public List<AdminResponseDto> ShowAdminPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.showAdminPage(userDetails);
    }

    //관리자 페이지 - 수정 기능



    // 관리자 페이지 삭제 기능
}