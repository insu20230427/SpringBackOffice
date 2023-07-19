package com.example.springbackoffice.jwt;

import com.example.springbackoffice.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil { // 다른 객체에 의존하지 않고 하나의 모듈로서 동작

    // ---------- JWT 데이터 -----------

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    //사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";

    //Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    //Token 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분 설정

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    //로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // JWT 생성
    public String createToken(String username, UserRoleEnum roleEnum) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, roleEnum) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료시간 : 현재시간 + 60분
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    //생성된 JWT를 Cookie에 저장
    public void addJwtToCookie(String token, HttpServletResponse httpServletResponse) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value에는 공백이 불가능하여 encoding 진행
            logger.info(token);
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name - Value
            cookie.setPath("/");

            //Response 객체에 Cookie 추가
            httpServletResponse.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    //Cookie에 들어있던 JWT 토큰을 Substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7); // BEARER_PREFIX 값인 "Bearer " 총 7자 절삭
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); //토큰 위변조 or 만료되진 않났는지 토큰을 검증
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT Signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰입니다.");
        }
        return false;
    }

    //JWT 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest에서 Cookiew Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public String resolveToken(HttpServletRequest request) { // HttpServletRequset 안에는 우리가 가져와야 할 토큰이 헤더에 들어있음
        String bearerToken = getTokenFromRequest(request);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // 코드가 있는지, BEARER로 시작하는지 확인
            return bearerToken.substring(7); // 앞에 7글자를 지워줌 BEARER가 6글자이고 한칸이 띄어져있기때문
        }
        return null;
    }


    // Token 체크
    public User checkToken(HttpServletRequest request){

        String token = resolveToken(request);
        Claims claims;

        if (token != null) {
            if (validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return user;

        }
        return null;
    }
}
