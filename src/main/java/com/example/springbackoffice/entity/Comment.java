package com.example.springbackoffice.entity;

import com.example.springbackoffice.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents; // 댓글 본문

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Integer commentLikedCount;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; //게시글과 연관 관계

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 글쓴이와 연관 관계

    public Comment(CommentRequestDto commentRequestDto, User user, Post post) {
        this.contents = commentRequestDto.getContents();
        this.username = user.getUsername();
        this.user = user;
        this.post = post;
        this.commentLikedCount = 0;
    }

    public void setCommentLikedCount(Integer commentLikedCount) {
        this.commentLikedCount = commentLikedCount;
    }
}