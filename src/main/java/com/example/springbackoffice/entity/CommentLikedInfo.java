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
    private boolean liked;

    public CommentLikedInfo() {

    }
    public CommentLikedInfo(Long commentId, Long userId) {
        this.commentId = commentId;
        this.userId = userId;
    }

    public boolean getLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
