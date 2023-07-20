package com.example.springbackoffice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_liked_info")
public class PostLikedInfo extends Timestamped {

    public PostLikedInfo() {
    }
    public PostLikedInfo(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean liked;

    public boolean getLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
