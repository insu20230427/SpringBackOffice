package com.example.springbackoffice.dto;

import com.example.springbackoffice.entity.User;
import com.example.springbackoffice.entity.UserRoleEnum;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AdminResponseDto {

    String username;
    String selfIntroduction;
    String email;
    UserRoleEnum role;
    private final List<PostResponseDto> postList;
    private final List<CommentResponseDto> commentList;

    public AdminResponseDto(User users) {
        this.username = users.getUsername();
        this.selfIntroduction = users.getSelfIntroduction();
        this.email = users.getEmail();
        this.role = users.getRole();
        this.postList = users.getPostList().stream()
                .map(post -> new PostResponseDto(post, post.getCommentList()))
                .collect(Collectors.toList());
        this.commentList = users.getCommentList().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}