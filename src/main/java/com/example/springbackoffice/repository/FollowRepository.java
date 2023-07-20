package com.example.springbackoffice.repository;

import com.example.springbackoffice.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {


    Integer countByFollowingIdAndFollowedIsTrue(Long followingId);

    Integer countByFollowerIdAndFollowedIsTrue(Long followerId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
