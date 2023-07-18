package com.example.springbackoffice.controller;

import com.example.springbackoffice.dto.ApiResponseDto;
import com.example.springbackoffice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    // 좋아요
    @PutMapping("/comments/{id}/like")
    public ResponseEntity<ApiResponseDto> addLikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        try {
            ApiResponseDto responseDto = commentService.addLikeComment(id, userDetails);
            return ResponseEntity.ok().body(responseDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.notFound().build();
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("자신의 댓글에는 좋아요를 할 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }
}
