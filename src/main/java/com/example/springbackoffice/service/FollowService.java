package com.example.springbackoffice.service;

import com.example.springbackoffice.dto.ApiResponseDto;
import com.example.springbackoffice.entity.Follow;
import com.example.springbackoffice.repository.FollowRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.RejectedExecutionException;

@Service
public class FollowService {
    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    // 팔로우
    @Transactional
    public ApiResponseDto follow(UserDetailsImpl userDetails, Long followingId) {
        Long followerId = userDetails.getUserId();

        // 팔로우 하려는 회원이 존재하는지 확인
        User User = UserRepository.findById(followingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."));
        // 자신에게 팔로우 X
        if (followerId.equals(followingId)) {
            throw new RejectedExecutionException("자신을 팔로우 할 수 없습니다.");
        }
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId).orElse(null);


        if (follow == null) {
            follow = new Follow(followerId, followingId);
            follow.setFollowed(true);
            followRepository.save(follow);
//            updatePostLikedCount(postId);
            return new ApiResponseDto("팔로우 되었습니다.", 200);
        } else {
            follow.setFollowed(!follow.getFollowed());
            followRepository.save(follow);
//            updatePostLikedCount(postId);
            if (follow.getFollowed()) {
                return new ApiResponseDto("팔로우 되었습니다", 200);
            } else {
                return new ApiResponseDto("팔로우가 취소되었습니다.", 200);
            }
        }
    }

}
