package com.example.springbackoffice.service;

import com.example.springbackoffice.dto.ApiResponseDto;
import com.example.springbackoffice.entity.Follow;
import com.example.springbackoffice.entity.User;
import com.example.springbackoffice.repository.FollowRepository;
import com.example.springbackoffice.repository.UserRepository;
import com.example.springbackoffice.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    // 팔로우
    @Transactional
    public ApiResponseDto follow(UserDetailsImpl userDetails, Long followingId) {
        Long followerId = userDetails.getUser().getId();

        // 팔로우 하려는 회원이 존재하는지 확인
        User followingUser = userRepository.findById(followingId)
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
            updateFollowCount(followerId, followingId);

            return new ApiResponseDto(200, "팔로우 되었습니다.");
        } else {
            follow.setFollowed(!follow.getFollowed());
            followRepository.save(follow);
            updateFollowCount(followerId, followingId);

            if (follow.getFollowed()) {
                return new ApiResponseDto(200, "팔로우 되었습니다");
            } else {
                return new ApiResponseDto(200, "팔로우가 취소되었습니다.");
            }
        }
    }
    // 추가 혹은 취소된 팔로워와 팔로잉 카운트
    private void updateFollowCount (Long followerId, Long followingId) {
        User followingUser = userRepository.findById(followingId).orElseThrow();
        User followerUser = userRepository.findById(followerId).orElseThrow();

        Integer followerCount = followRepository.countByFollowingIdAndFollowedIsTrue(followingId);
        Integer followingCount = followRepository.countByFollowerIdAndFollowedIsTrue(followerId);

        followingUser.setFollowerCount(followerCount);
        followerUser.setFollowingCount(followingCount);
    }

    // 특정 유저의 팔로워 목록 조회
    public List<String > getFollowerList(Long userId) {
        List<Follow> followerList = followRepository.findByFollowingId(userId);
        return followerList.stream()
                .map(follow -> userRepository.findById(follow.getFollowerId()).map(User::getUsername).orElse(null))
                .collect(Collectors.toList());
    }

    // 특정 유저의 팔로잉 목록 조회
    public List<String > getFollowingList(Long userId) {
        List<Follow> followingList = followRepository.findByFollowerId(userId);
        return followingList.stream()
                .map(follow -> userRepository.findById(follow.getFollowingId()).map(User::getUsername).orElseThrow())
                .collect(Collectors.toList());
    }
}
