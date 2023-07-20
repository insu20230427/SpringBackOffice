package com.example.springbackoffice.controller;

import com.example.springbackoffice.dto.*;
import com.example.springbackoffice.security.UserDetailsImpl;
import com.example.springbackoffice.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AdminController {

    private final AdminService adminService;

    // 관리자 페이지 (유저 + 글 + 댓글)
    @GetMapping("/admin/page")
    public List<AdminResponseDto> ShowAdminPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.showAdminPage(userDetails);
    }

    // 유저 전체 정보 조회
    @GetMapping("/admin/show-users")
    public List<UserResponseDto> showUsers (@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.showUsers(userDetails);
    }

    // 글 전체 정보 조회
    @GetMapping("/admin/show-posts")
    public List<PostResponseDto> showPosts (@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.showPosts(userDetails);
    }

    // 댓글 전체 정보 조회
    @GetMapping("/admin/show-comments")
    public List<CommentResponseDto> showComments (@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.showComments(userDetails);
    }



    //관리자 페이지 - 수정 기능
//    @PutMapping(/admin/page)
//    public


    // 관리자 페이지 삭제 기능
}