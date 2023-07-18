package com.example.springbackoffice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comment_liked_info")
public class CommentLikedInfo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_liked_id")
    private Long id;
    private Long commentId;
    private String username;
    private Boolean isLiked;

    public CommentLikedInfo() {

    }
    public CommentLikedInfo(Long commentId, String username) {
        this.commentId = commentId;
        this.username = username;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }
}
