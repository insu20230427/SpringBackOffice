package com.example.springbackoffice.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "follows")
public class Follow extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private Long followerId;

    @Column(nullable = false)
    private Long followingId;

    @Column(nullable = false)
    private boolean followed;

    public Follow() {

    }
    public Follow(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public boolean getFollowed() {
        return followed;
    }
}