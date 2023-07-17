package com.example.springbackoffice.service;

import com.example.springbackoffice.dto.ApiResult;
import com.example.springbackoffice.dto.ProfileEditRequestDto;
import com.example.springbackoffice.entity.User;
import com.example.springbackoffice.jwt.JwtUtil;
import com.example.springbackoffice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static final String ADMIN_TOKEN = "AAAABnvqaqwetK20aTBZ25hqkD";

    //회원 정보 변경
    @Transactional
    public ApiResult editProfile (ProfileEditRequestDto profileEditRequestDto, HttpServletRequest httpServletRequest) {

        User user = checkToken(httpServletRequest);

        String password = profileEditRequestDto.getPassword();
        String introduction = profileEditRequestDto.getIntroduction();

        if (!passwordEncoder.matches(password, user.getPassword())) { // 첫번째 파라미터는 encoding 안된 비밀번호, 두번째는 encoding된 난수 비밀번호
            return new ApiResult("기존 비밀번호를 잘못 입력하셨습니다.", HttpStatus.BAD_REQUEST);
        }
        String changepassword = passwordEncoder.encode(profileEditRequestDto.getChangepassword());

        user.setPassword(changepassword);
        user.setIntroduction(introduction);
        userRepository.saveAndFlush(user);
        return new ApiResult("프로필 변경에 성공했습니다", HttpStatus.ACCEPTED);
    }

    // JWT 토큰 체크
    public User checkToken(HttpServletRequest httpServletRequest){

        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
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
