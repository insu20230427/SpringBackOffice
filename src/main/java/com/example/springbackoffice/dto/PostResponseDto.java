package com.example.springbackoffice.dto;

import com.example.springbackoffice.entity.Comment;
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
// 리스트 C 대문자로 수정
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.postLikeCount = post.getPostLikeCount();
        this.user_id = post.getUser().getId();
        this.userName = post.getUsername();
        this.postCommentList = post.getCommentList() // 댓글 목록 조회
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
//        if(post.getCommentList().size()>0) {
//            this.commentResponseDtoList = new ArrayList<>();
//            for (Comment comment : post.getCommentList()) {
//                this.commentResponseDtoList.add(new CommentResponseDto(comment));
//            }
            // getComments를 통해 저장된 post의 commentList의 comment들을
            // PostResponseDto의 commentResponseDtoList에 복사하는 과정
    }

    public PostResponseDto (Post post, List<Comment> commentList) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.postLikeCount = post.getPostLikeCount();
        this.user_id = post.getUser().getId();
        this.userName = post.getUsername();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.postCommentList = commentList.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}


