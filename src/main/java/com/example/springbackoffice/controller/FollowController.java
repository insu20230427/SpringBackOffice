package com.example.springbackoffice.controller;

import com.example.springbackoffice.dto.ApiResponseDto;
import com.example.springbackoffice.security.UserDetailsImpl;
import com.example.springbackoffice.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PutMapping("/follow/{followingId}")
    public ResponseEntity<ApiResponseDto> follow(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long followingId) {
        try {
            ApiResponseDto responseDto = followService.follow(userDetails, followingId);
            return ResponseEntity.ok().body(responseDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.notFound().build();
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "자신을 팔로우 할 수 없습니다."));
        }
    }
}
