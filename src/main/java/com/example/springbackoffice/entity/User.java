package com.example.springbackoffice.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "user_password", nullable = false)
    private String password;

    // 이메일 인증 구현시 필요 엔티티
//    @Column(name = "user_email", nullable = false,unique = true)
//    private String email;

    // 추후 기능 구현에 쓸 수 있음 (현재는 X)
    @Column(name = "self_introduction")
    private String selfIntroduction;

    @Column(name = "user_role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

//    이메일 인증 구현하려면 필요한 엔티티
//    @Column(name="user_confirmn",nullable = false)
//    @ColumnDefault("false")
//    private Boolean isConfirm;

    public User(String username, String password,UserRoleEnum role) {
        this.username = username;
        this.password = password;
//        this.email = email;
//        쓰려면 User 안에 String email 추가
        this.role = role;
//        this.isConfirm=false;
    }
}
