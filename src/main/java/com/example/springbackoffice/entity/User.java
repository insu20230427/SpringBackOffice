package com.example.springbackoffice.entity;

import com.example.springbackoffice.dto.ProfileEditRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    String password;

    @Column (name = "email", nullable = false, unique = true)
    String email;

    @Column (name = "introduction")
    String introduction;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRoleEnum;

    public User(String username, ProfileEditRequestDto profileEditRequestDto, String email, UserRoleEnum userRoleEnum) {
        this.username = username;
        this.password = profileEditRequestDto.getChangepassword();
        this.email = email;
        this.introduction = profileEditRequestDto.getIntroduction();
        this.userRoleEnum = userRoleEnum;
    }

}
