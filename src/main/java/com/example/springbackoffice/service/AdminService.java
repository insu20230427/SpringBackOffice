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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public AdminSummaryResponseDto showSummary(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);

        Long allUsersCount = userRepository.count();
        Long allPostsCount = postRepository.count();
        Long allCommentsCount = commentRepository.count();

        return new AdminSummaryResponseDto(allUsersCount, allPostsCount, allCommentsCount);
    }


    // 관리자 페이지 (유저 + 글 + 댓글 전체 조회)
    public List<AdminResponseDto> showAdminPage(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(AdminResponseDto::new) // User 객체를 AdminResponseDto로 변환 (로직 이해하기!!)
                .collect(Collectors.toList());
    }

    // 유저 전체 정보 조회
    public List<UserResponseDto> showUsers(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    // 관리자 - 전체 게시글 조회
    public List<PostResponseDto> showPosts(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);

        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 관리자 - 전체 댓글 조회
    public List<CommentResponseDto> showComments(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);

        List<Comment> comments = commentRepository.findAll();

        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 관리자 - 유저 정보 변경
    @Transactional
    public ApiResponseDto editUserProfile(Long id, SignupRequestDto signupRequestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);

        Optional<User> optionalUser = userRepository.findById(id);

        // 해당 id에 해당하는 유저가 존재하는지 검증.
        if (optionalUser.isPresent()) {
            // 유저가 존재하는 경우에만 아래 로직을 수행.
            User foundUser = optionalUser.get();

            if (passwordEncoder.matches(signupRequestDto.getPassword(), foundUser.getPassword())) {
                return new ApiResponseDto(400, "같은 비밀번호로 변경할 수 없습니다.", HttpStatus.BAD_REQUEST);
            }

            if (userRepository.findByUsername(signupRequestDto.getUsername()).isPresent()) {
                return new ApiResponseDto(400, "해당 유저는 이미 존재합니다.", HttpStatus.BAD_REQUEST);
            }

            foundUser.setUsername(signupRequestDto.getUsername());
            foundUser.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
            foundUser.setEmail(signupRequestDto.getEmail());
            foundUser.setSelfIntroduction(signupRequestDto.getSelfIntroduction());

            userRepository.save(foundUser);

            return new ApiResponseDto(202, "유저 정보를 수정하였습니다.", HttpStatus.ACCEPTED);
        } else {
            // 해당 id에 해당하는 유저가 존재하지 않는 경우 에러를 Api 반환.
            return new ApiResponseDto(400, "해당 유저는 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // 괸리자 - 유저 삭제
    @Transactional
    public ApiResponseDto deleteUserProfile(Long id, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);
        Optional<User> optionalUser = userRepository.findById(id);

        // 해당 id에 해당하는 유저가 존재하는지 검증.
        if (optionalUser.isPresent()) {
            // 유저가 존재하는 경우에만 아래 로직을 수행.
            User foundUser = optionalUser.get();
            userRepository.delete(foundUser);
            return new ApiResponseDto(400, "해당 유저는 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        } else {
            return new ApiResponseDto(202, "유저를 삭제 하였습니다.", HttpStatus.ACCEPTED);
        }
    }

    // 관리자 - 글 수정 기능
    @Transactional
    public ApiResponseDto editPost(Long id, PostResponseDto postResponseDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);

        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isPresent()) {
            Post foundPost = optionalPost.get();

            foundPost.setTitle(postResponseDto.getTitle());
            foundPost.setContents(postResponseDto.getContents());
            foundPost.setUsername(postResponseDto.getUserName());
            foundPost.setPostLikeCount(postResponseDto.getPostLikeCount());

            postRepository.save(foundPost);
        } else {
            return new ApiResponseDto(400, "해당 게시글은 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ApiResponseDto(202, "해당 게시글 내용을 수정하였습니다.", HttpStatus.ACCEPTED);
    }

     // 관리자 - 글 삭제 기능
     @Transactional
     public ApiResponseDto deletePost (Long id, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);

        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isPresent()) {
            Post foundPost = optionalPost.get();
            postRepository.delete(foundPost);
            return new ApiResponseDto(202, "게시글을 성공적으로 삭제하였습니다.", HttpStatus.ACCEPTED);
        } else {
            return new ApiResponseDto(400, "해당 게시글은 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // 관리자 - 댓글 수정 기능
    @Transactional
    public ApiResponseDto editComment(Long id, CommentResponseDto commentResponseDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);

        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isPresent()) {
            Comment foundComment = optionalComment.get();

            foundComment.setUsername(commentResponseDto.getUsername());
            foundComment.setContents(commentResponseDto.getContents());
            foundComment.setCommentLikedCount(commentResponseDto.getCommentLikeCount());

            commentRepository.save(foundComment);
        } else {
            return new ApiResponseDto(400, "해당 댓글은 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ApiResponseDto(202, "해당 댓글 내용을 수정하였습니다.", HttpStatus.ACCEPTED);
    }

    // 관리자 - 댓글 삭제 기능
    @Transactional
    public ApiResponseDto deleteComment(Long id, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        checkRole(user);

        Optional<Comment> optionalComment = commentRepository.findById(id);

        // 해당 id에 해당하는 유저가 존재하는지 검증.
        if (optionalComment.isPresent()) {
            // 유저가 존재하는 경우에만 아래 로직을 수행.
            Comment foundComment = optionalComment.get();
            commentRepository.delete(foundComment);
            return new ApiResponseDto(400, "해당 유저는 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        } else {
            return new ApiResponseDto(202, "유저를 삭제 하였습니다.", HttpStatus.ACCEPTED);
        }
    }

    //관리자 권한 체크
    public void checkRole(User user) {
        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }
    }
}