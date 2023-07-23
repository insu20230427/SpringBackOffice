package com.example.springbackoffice.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "user_password", nullable = false)
    private String password;

    // 이메일 인증 구현시 필요 엔티티
    @Column(name = "user_email", nullable = false,unique = true)
    private String email;

    // 추후 기능 구현에 쓸 수 있음 (현재는 X)
    @Column(name = "self_introduction")
    private String selfIntroduction;

    @Column(name = "user_role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private Integer followerCount;

    @Column(nullable = false)
    private Integer followingCount;
//    이메일 인증 구현하려면 필요한 엔티티
//    @Column(name="user_confirmn",nullable = false)
//    @ColumnDefault("false")
//    private Boolean isConfirm;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    private  Long kakaoId;

    public User(String username,String password, String email, UserRoleEnum role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
//        쓰려면 User 안에 String email 추가
        this.role = role;
//        this.isConfirm=false;
        this.followerCount = 0;
        this.followingCount = 0;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

}
