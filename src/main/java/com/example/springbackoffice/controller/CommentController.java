package com.example.springbackoffice.controller;

import com.example.springbackoffice.dto.ApiResponseDto;
import com.example.springbackoffice.dto.CommentRequestDto;
import com.example.springbackoffice.dto.CommentResponseDto;
import com.example.springbackoffice.security.UserDetailsImpl;
import com.example.springbackoffice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{id}") // 댓글 생성
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal
                                                            UserDetailsImpl userDetails, @PathVariable Long id,
                                                            @RequestBody CommentRequestDto requestDto){

        CommentResponseDto commentResult = commentService.createComment(requestDto,id,userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(commentResult);
    }

//
//    @GetMapping("/comments/{id}")
//    public String getComments(Model model){
//        List<CommentListViewResponseDto> commentListViewResponseDtos = commentService.findAll().stream()
//                .map(CommentListViewResponseDto::new)
//                .toList();
//        model.addAttribute("comments", commentListViewResponseDtos);
//        return "posts";
//    }

    @PutMapping("/comments/{id}") // 댓글 수정
    public ResponseEntity<ApiResponseDto> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        try {
            CommentResponseDto commentResult = commentService.updateComment(id, requestDto, userDetails.getUser());
            return ResponseEntity.ok().body(commentResult);

        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "작성자만 수정 할 수 있습니다."));
        }
    }

    @DeleteMapping("/comments/{id}") //댓글 삭제
    public ResponseEntity<ApiResponseDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        try {
            commentService.deleteComment(id, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "댓글 삭제 성공"));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "작성자만 삭제 할 수 있습니다."));
        }
    }

    // 좋아요
    @PutMapping("/comments/{id}/like")
    public ResponseEntity<ApiResponseDto> addLikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        try {
            ApiResponseDto responseDto = commentService.addLikeComment(id, userDetails);
            return ResponseEntity.ok().body(responseDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.notFound().build();
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "자신의 댓글에는 좋아요를 할 수 없습니다."));
        }
    }
}
