package com.example.springbackoffice.controller;


import com.example.springbackoffice.dto.ApiResponseDto;
import com.example.springbackoffice.dto.PostRequestDto;
import com.example.springbackoffice.dto.PostResponseDto;
import com.example.springbackoffice.security.UserDetailsImpl;
import com.example.springbackoffice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @PostMapping("/post") // 글 작성
    @ResponseBody
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser());
    }


    @GetMapping("/posts") // 전체 게시글 조회
    @ResponseBody
    public List<PostResponseDto> getPosts() { // 전체 게시글이므로 List형식으로 받아오기
        return postService.getPosts();
    }


    @GetMapping("/post/{id}") // 상세 게시글 조회
    @ResponseBody
    public PostResponseDto lookupPost(@PathVariable Long id) {
        return postService.lookupPost(id);
        // 1. 해당 id의 Post를 받아오는 메서드 호출
        // 2. 찾아온 Post를 반환시켜주기 위해 PostResponseDto에 넣어줌
        // 3. 저장된 commentList를 CommentResponseDtoList에 복사하여 PostResponseDto를 가져와 클라이언트에 반환
    }

    @PutMapping("/post/{id}") // 상세 게시글 수정
    public ResponseEntity<ApiResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id,postRequestDto,userDetails.getUser());
    }

    @DeleteMapping("/post/{id}") // 상세 게시글 삭제
    @ResponseBody
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getUser());
    }
}

