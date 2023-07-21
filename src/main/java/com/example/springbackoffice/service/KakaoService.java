package com.example.springbackoffice.service;

import com.example.springbackoffice.jwt.JwtUtil;
import com.example.springbackoffice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.springbackoffice.dto.KakaoUserInfoDto;
import com.example.springbackoffice.jwt.JwtUtil;
import com.example.springbackoffice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate; // 초기에 빨간불이 뜸. 빈으로 수동등록을 해야함. 빈으로 수동등록 하지 않으면, 따로 생성자에서 넣어주는 코드가 필요함.
    // 빈으로 수동등록 하는 방법
    // config 패키지에서 RestTemplateConfig 클래스 생성 (그 이후는 config/RestTemplateConfig 파일 참조)
    // RestTemplateConfig 를 통해 수동으로 빈을 등록하면, 상세 설정을 개발자가 임의로 지정할 수 있다.
    private final JwtUtil jwtUtil;

    public String kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        return null;
    }







}
