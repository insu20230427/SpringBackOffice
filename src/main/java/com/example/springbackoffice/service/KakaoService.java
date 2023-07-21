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

    // 액세스 토큰 요청
    // 받아온 인증코드로 추가적인 토큰 요청 - > 토큰 전달 받기 - > 전달 받은 토큰으로 API 호출하여 토큰 유효성 확인
    private String getToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "a573f56cbb9621b46e73849496e11cff");
        body.add("redirect_uri", "http://localhost:8080/api/user/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }
//  카카오톡 사용자 정보 JSON 의 예 (참조)

//    {
//        "id": 1632335751,
//            "properties": {
//        "nickname": "르탄이",
//                "profile_image": "http://k.kakaocdn.net/...jpg",
//                "thumbnail_image": "http://k.kakaocdn.net/...jpg"
//    },
//        "kakao_account": {
//        "profile_needs_agreement": false,
//                "profile": {
//            "nickname": "르탄이",
//                    "thumbnail_image_url": "http://k.kakaocdn.net/...jpg",
//                    "profile_image_url": "http://k.kakaocdn.net/...jpg"
//        },
//        "has_email": true,
//                "email_needs_agreement": false,
//                "is_email_valid": true,
//                "is_email_verified": true,
//                "email": "letan@sparta.com"
//    }
//    }





}
