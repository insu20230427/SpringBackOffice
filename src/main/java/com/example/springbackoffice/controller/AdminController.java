package com.example.springbackoffice.controller;

import com.example.springbackoffice.dto.*;
import com.example.springbackoffice.security.UserDetailsImpl;
import com.example.springbackoffice.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AdminController {

    // RequestParam 전체에서 불러오는 조건 설정!! -> required 설정을 true or false 설정 가능 보통은 false 설정해놓고 RequestParam을 보내지 않으면 전체 return 하는 방식으로 처리

    private final AdminService adminService;

    // 관리자 페이지 요약 (유저 수, 글 수, 댓글 수)
    @GetMapping("/admin/summary")
    public AdminSummaryResponseDto showSummary(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.showSummary(userDetails);
    }

    // 관리자 페이지 - (유저 + 글 + 댓글)
    @GetMapping("/admin/page")
    public List<AdminResponseDto> showAdminPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.showAdminPage(userDetails);
    }

    // 유저 전체 정보 조회
    @GetMapping("/admin/show-users")
    public List<UserResponseDto> showUsers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.showUsers(userDetails);
    }

    // 글 전체 정보 조회
    @GetMapping("/admin/show-posts")
    public List<PostResponseDto> showPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.showPosts(userDetails);
    }

    // 댓글 전체 정보 조회
    @GetMapping("/admin/show-comments")
    public List<CommentResponseDto> showComments(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.showComments(userDetails);
    }

    //관리자 페이지 - 유저 수정 기능
    @PutMapping("/admin/user/{id}")
    public ApiResponseDto editUserProfile(@PathVariable Long id, @RequestBody SignupRequestDto signupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
       return adminService.editUserProfile(id, signupRequestDto, userDetails);
    }

    // 관리자 페이지 - 유저 삭제 기능
    @DeleteMapping("/admin/user/{id}")
    public ApiResponseDto deleteUserProfile(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.deleteUserProfile(id, userDetails);
    }

    // 관리자 페이지 - 글 수정 기능
    @PutMapping("/admin/post/{id}")
    public ApiResponseDto editPost(@PathVariable Long id, @RequestBody PostResponseDto postResponseDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.editPost(id, postResponseDto, userDetails);
    }

    // 관리자 페이지 글 삭제 기능
    @DeleteMapping("/admin/post/{id}")
    public ApiResponseDto deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.deletePost(id, userDetails);
    }

    // 관리자 페이지 댓글 수정 기능
    @PutMapping("/admin/comment/{id}")
    public ApiResponseDto editComment (@PathVariable Long id, @RequestBody CommentResponseDto commentResponseDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.editComment(id, commentResponseDto, userDetails);
    }

    // 관리자 페이지 댓글 삭제 기능
    @DeleteMapping("/admin/comment/{id}")
    public ApiResponseDto deleteComment (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.deleteComment(id, userDetails);
    }
}