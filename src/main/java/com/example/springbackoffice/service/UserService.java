package com.example.springbackoffice.service;

import com.example.springbackoffice.dto.*;
import com.example.springbackoffice.entity.PasswordHistory;
import com.example.springbackoffice.entity.TokenBlacklist;
import com.example.springbackoffice.entity.User;
import com.example.springbackoffice.entity.UserRoleEnum;
import com.example.springbackoffice.repository.PasswordRepository;
import com.example.springbackoffice.repository.TokenBlacklistRepository;
import com.example.springbackoffice.repository.UserRepository;
import com.example.springbackoffice.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordRepository passwordRepository;

    private final UserRepository userRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    //로그아웃
    @Transactional
    public void logout(String token) {
        // 토큰을 블랙리스트에 추가
        TokenBlacklist tokenBlacklist = new TokenBlacklist(token);
        tokenBlacklistRepository.save(tokenBlacklist);
    }

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
//        String authkey = requestDto.getAuthKey();
        User user = new User(username, password, email, role);
        userRepository.save(user);
    }
    //회원 정보 조회
    @Transactional (readOnly = true)
    public ProfileResponseDto showProfile(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        return new ProfileResponseDto(user);
    }

    //회원 정보 변경
    @Transactional
    public ApiResponseDto editProfile (ProfileEditRequestDto profileEditRequestDto, UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        String password = profileEditRequestDto.getPassword();
        String introduction = profileEditRequestDto.getSelfIntroduction();
        String changePassword = profileEditRequestDto.getChangepassword();;

        if (!passwordEncoder.matches(password, user.getPassword())) { // 첫번째 파라미터는 encoding 안된 비밀번호, 두번째는 encoding된 난수 비밀번호
            return new ApiResponseDto("기존 비밀번호를 잘못 입력하셨습니다.", HttpStatus.BAD_REQUEST);
        }

        if (Objects.equals(password, changePassword)) {
            return new ApiResponseDto("같은 비밀번호로는 변경할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        //최근 3회 비밀번호 가져오기
        List<PasswordHistory> passwordHistoryList = passwordRepository.findTop3ByUserOrderByModifiedAtDesc(user);

        for (PasswordHistory passwordHistory : passwordHistoryList) {
            if (passwordEncoder.matches(changePassword, passwordHistory.getPassword())) {
                return new ApiResponseDto("최근 3번동안 사용한 비밀번호는 사용이 불가능합니다.", HttpStatus.BAD_REQUEST);
            }
        }

        //현재 비밀번호를 PasswordRepository에 저장
        PasswordHistory currentPasswordHistory = new PasswordHistory();
        currentPasswordHistory.setUser(user);
        currentPasswordHistory.setPassword(user.getPassword());
        passwordRepository.save(currentPasswordHistory);

        // 히스토리에서 3번째 전 비밀번호 삭제
        if (passwordHistoryList.size() >= 3) {
            PasswordHistory deletePasswordHistory = passwordHistoryList.get(passwordHistoryList.size() - 1);
            passwordRepository.delete(deletePasswordHistory);
        }

        String newPassword = passwordEncoder.encode(changePassword);

        user.setPassword(newPassword);
        user.setSelfIntroduction(introduction);
        userRepository.save(user);
        return new ApiResponseDto("프로필 변경에 성공했습니다", HttpStatus.ACCEPTED);
    }
}
