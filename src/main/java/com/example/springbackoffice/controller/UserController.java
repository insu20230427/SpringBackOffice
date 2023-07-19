package com.example.springbackoffice.controller;

import com.example.springbackoffice.dto.ApiResult;
import com.example.springbackoffice.dto.ProfileEditRequestDto;
import com.example.springbackoffice.dto.ProfileResponseDto;
import com.example.springbackoffice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;



    //회원정보 조회 API
    @GetMapping("/users/profile")
    public ProfileResponseDto showProfile(HttpServletRequest httpServletRequest) {
        return userService.showProfile(httpServletRequest);
    }

    //회원정보 수정 API
    @PutMapping("/users-profile")
    public ApiResult editProfile (@RequestBody ProfileEditRequestDto profileEditRequestDto, HttpServletRequest httpServletRequest) {
      return userService.editProfile(profileEditRequestDto, httpServletRequest);
    }
}
