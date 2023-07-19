package com.example.springbackoffice.service;

import com.example.springbackoffice.dto.ApiResult;
import com.example.springbackoffice.dto.ProfileEditRequestDto;
import com.example.springbackoffice.dto.ProfileResponseDto;
import com.example.springbackoffice.entity.PasswordHistory;
import com.example.springbackoffice.entity.User;
import com.example.springbackoffice.jwt.JwtUtil;
import com.example.springbackoffice.repository.PasswordRepository;
import com.example.springbackoffice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;

    //회원 정보 조회
    @Transactional (readOnly = true)
    public ProfileResponseDto showProfile(HttpServletRequest httpServletRequest ) {
        User user = jwtUtil.checkToken(httpServletRequest);

        return new ProfileResponseDto(user);
    }

    //회원 정보 변경
    @Transactional
    public ApiResult editProfile (ProfileEditRequestDto profileEditRequestDto, HttpServletRequest httpServletRequest) {

        User user = jwtUtil.checkToken(httpServletRequest);

        String password = profileEditRequestDto.getPassword();
        String introduction = profileEditRequestDto.getSelfInroduction();
        String changePassword = profileEditRequestDto.getChangepassword();;

        if (!passwordEncoder.matches(password, user.getPassword())) { // 첫번째 파라미터는 encoding 안된 비밀번호, 두번째는 encoding된 난수 비밀번호
            return new ApiResult("기존 비밀번호를 잘못 입력하셨습니다.", HttpStatus.BAD_REQUEST);
        }

        //최근 3회 비밀번호 가져오기
        List<PasswordHistory> passwordHistoryList = passwordRepository.findTop3ByUserOrderByModifiedAtDesc(user);

        for (PasswordHistory passwordHistory : passwordHistoryList) {
            if (passwordEncoder.matches(changePassword, passwordHistory.getPassword())) {
                return new ApiResult("최근 3번동안 사용한 비밀번호는 사용이 불가능합니다.", HttpStatus.BAD_REQUEST);
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
        return new ApiResult("프로필 변경에 성공했습니다", HttpStatus.ACCEPTED);
    }
}
