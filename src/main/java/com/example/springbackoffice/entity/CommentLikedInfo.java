package com.example.springbackoffice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comment_liked_info")
public class CommentLikedInfo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private Long commentId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Boolean liked;

    public CommentLikedInfo() {

    }
    public CommentLikedInfo(Long commentId, Long userId) {
        this.commentId = commentId;
        this.userId = userId;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }
}
