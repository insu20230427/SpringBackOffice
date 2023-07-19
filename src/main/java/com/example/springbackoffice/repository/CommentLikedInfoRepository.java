package com.example.springbackoffice.repository;

import com.example.springbackoffice.entity.CommentLikedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikedInfoRepository extends JpaRepository<CommentLikedInfo, Long> {
    Integer countByCommentIdAndLikedIsTrue(Long commentId);

    Optional<CommentLikedInfo> findByCommentIdAndUserId(Long commentId, Long userId);
}
