package com.example.springbackoffice.controller;

import com.example.springbackoffice.dto.*;
import com.example.springbackoffice.jwt.JwtUtil;
import com.example.springbackoffice.security.UserDetailsImpl;
import com.example.springbackoffice.service.FollowService;
import com.example.springbackoffice.service.KakaoService;
import com.example.springbackoffice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final FollowService followService;
    private final KakaoService kakaoService;
//    private final MailSenderService mailSenderService;

    //회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponseDto> signup(@RequestBody SignupRequestDto requestDto) { //클라이언트로부터 SignupRequestDto 를 요청 RequestBody 로 받아와서 처리

        try {
//            String authKey = mailSenderService.sendSimpleMessage(requestDto.getEmail());
//            requestDto.setAuthKey(authKey);
            userService.signup(requestDto); //회원 가입을 처리하기 위해 userService.signup(requestDto)를 호출
        } catch (IllegalArgumentException e) { // 중복된 username 이 있는 경우
            return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 id 입니다. 다른 id를 입력해 주세요"));
        }
        return ResponseEntity.status(201).body(new ApiResponseDto(HttpStatus.CREATED.value(), "회원가입 완료 되었습니다."));
    }
    //로그인
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody AuthRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            userService.login(loginRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage()));
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(loginRequestDto.getUsername(), loginRequestDto.getRole()));

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "로그인 성공"));
    }

    //회원정보 조회 API
    @GetMapping("/auth/profile")
    public ProfileResponseDto showProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.showProfile(userDetails);
    }

    //회원정보 수정 API
    @PutMapping("/auth/edit-profile")
    public ApiResponseDto editProfile (@RequestBody ProfileEditRequestDto profileEditRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.editProfile(profileEditRequestDto, userDetails);
    }
    @GetMapping("/follow/followers/{userId}")
    public ResponseEntity<List<String>> getFollowers(@PathVariable Long userId) {
        List<String> followers = followService.getFollowerList(userId);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @GetMapping("/follow/followings/{userId}")
    public ResponseEntity<List<String>> getFollowing(@PathVariable Long userId) {
        List<String> following = followService.getFollowingList(userId);
        return new ResponseEntity<>(following, HttpStatus.OK);
    }

    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드 Service 전달 후 인증 처리 및 JWT 반환
        String token = kakaoService.kakaoLogin(code);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }
}