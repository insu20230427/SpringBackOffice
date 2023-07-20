package com.example.springbackoffice.service;

import com.example.springbackoffice.dto.*;
import com.example.springbackoffice.entity.Comment;
import com.example.springbackoffice.entity.Post;
import com.example.springbackoffice.entity.User;
import com.example.springbackoffice.entity.UserRoleEnum;
import com.example.springbackoffice.jwt.JwtUtil;
import com.example.springbackoffice.repository.CommentRepository;
import com.example.springbackoffice.repository.PostRepository;
import com.example.springbackoffice.repository.UserRepository;
import com.example.springbackoffice.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;


    // 관리자 페이지 (유저 + 글 + 댓글 전체 조회)
    @Transactional
    public List<AdminResponseDto> showAdminPage(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        List<User> users = userRepository.findAll();

        List<AdminResponseDto> adminResponseDtos = users.stream()
                .map(AdminResponseDto::new) // User 객체를 AdminResponseDto로 변환 (로직 이해하기!!)
                .collect(Collectors.toList());

        return adminResponseDtos;
    }

    // 유저 전체 정보 조회
    public List<UserResponseDto> showUsers(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        List<User> users = userRepository.findAll();

        List<UserResponseDto> userResponseDtos = users.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());

        return userResponseDtos;
    }

    public List<PostResponseDto> showPosts(UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        List<Post> posts = postRepository.findAll();

        List<PostResponseDto> postResponseDtos = posts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return postResponseDtos;
    }

    public List<CommentResponseDto> showComments(UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        List<Comment> comments = commentRepository.findAll();

        List<CommentResponseDto> commentResponseDtos = comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());

        return commentResponseDtos;
    }



    // 글 전체 정보 조회


    // 댓글 전체 정보 조회
}