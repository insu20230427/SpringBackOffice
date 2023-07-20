package com.example.springbackoffice.dto;

import com.example.springbackoffice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponseDto {

    private String username;
    private String selfIntroduction;
    private List<PostResponseDto> blogList;
    private List<CommentResponseDto> commentList;



    // Profile 변경 ResponseDto 현재 프로필 조회시 BlogResponseDto와 CommentResponseDto의 각각 생성자에 맞는 필드가 나오는 것이 아닌, 모든 필드가 나옴
    public ProfileResponseDto(User user) {
        this.username = user.getUsername();
        this.selfIntroduction = user.getSelfIntroduction();
        this.blogList = user.getPostList().stream()
                .map(blog -> new PostResponseDto(blog, blog.getCommentList()))
                .collect(Collectors.toList());
        this.commentList = user.getCommentList().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
