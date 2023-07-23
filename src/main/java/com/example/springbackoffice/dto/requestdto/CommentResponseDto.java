package com.example.springbackoffice.dto.requestdto;

import com.example.springbackoffice.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {

    private Long id;
    private String contents; // 본문
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer commentLikeCount;
    public CommentResponseDto(Comment comment) {

        this.id = comment.getId();
        this.contents = comment.getContents();
        this.username = comment.getUser().getUsername();
        this.createdAt =comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.commentLikeCount = comment.getCommentLikedCount();
    }
}
