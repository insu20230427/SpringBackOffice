package com.example.springbackoffice.repository;

import com.example.springbackoffice.entity.PostLikedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikedInfoRepository extends JpaRepository<PostLikedInfo, Long> {
    Optional<PostLikedInfo> findByPostIdAndUserId(Long postId, Long userId);

    Integer countByPostIdAndLikedIsTrue(Long postId);


}
