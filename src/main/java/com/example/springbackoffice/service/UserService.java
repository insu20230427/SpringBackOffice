package com.example.springbackoffice.service;

import com.example.springbackoffice.dto.AuthRequestDto;
import com.example.springbackoffice.dto.SignupRequestDto;
import com.example.springbackoffice.entity.User;
import com.example.springbackoffice.entity.UserRoleEnum;
import com.example.springbackoffice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    // BlackList 를 저장할 Repository
//    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 로그아웃
//    @Transactional
//    public void logout(String token) {
//        // 토큰을 블랙리스트에 추가
//        TokenBlacklist tokenBlacklist = new TokenBlacklist(token);
//        tokenBlacklistRepository.save(tokenBlacklist);
//    }

    //로그인
    public void login(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException(" 등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 id 입니다. 다른 id를 입력해 주세요");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        String authkey = requestDto.getAuthKey();
        User user = new User(username, password, role);
        userRepository.save(user);
    }
}
