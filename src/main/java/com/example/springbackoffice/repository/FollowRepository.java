package com.example.springbackoffice.repository;

import com.example.springbackoffice.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {


    Integer countByFollowingIdAndFollowedIsTrue(Long followingId);

    Integer countByFollowerIdAndFollowedIsTrue(Long followerId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    List<Follow> findByFollowingId(Long id);

    List<Follow> findByFollowerId(Long userId);
}
