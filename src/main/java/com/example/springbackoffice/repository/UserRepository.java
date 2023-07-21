package com.example.springbackoffice.repository;

import com.example.springbackoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByKakaoId(Long kakaoId);
    //인증이 된 유저만 이름으로 조회
     Optional<User> findByEmail(String email);
}