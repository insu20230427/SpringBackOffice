package com.example.springbackoffice.dto;


import com.example.springbackoffice.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDto {

    private String username;

    private  String password;

    private UserRoleEnum role; // 회원 권한 (admin, user)



}
