package com.example.springbackoffice.dto.responsedto;

import com.example.springbackoffice.dto.requestdto.CommentResponseDto;
import com.example.springbackoffice.entity.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long user_id;
    private String userName;
    private Integer postLikeCount;
    private List<CommentResponseDto> postCommentList;

    //    private List<CommentResponseDto> postCommentList;
// 리스트 C 대문자로 수정
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.user_id = post.getUser().getId();
        this.userName = post.getUsername();
        this.postLikeCount = post.getPostLikeCount();
        this.postCommentList = post.getCommentList().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}

