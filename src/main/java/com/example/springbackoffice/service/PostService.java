package com.example.springbackoffice.service;

import com.example.springbackoffice.dto.ApiResponseDto;
import com.example.springbackoffice.entity.PostLikedInfo;
import com.example.springbackoffice.repository.PostLikedInfoRepository;
import com.example.springbackoffice.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.RejectedExecutionException;
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostLikedInfoRepository postLikedInfoRepository;

    public PostService(PostRepository postRepository, PostLikedInfoRepository postLikedInfoRepository) {
        this.postRepository = postRepository;
        this.postLikedInfoRepository = postLikedInfoRepository;
    }
    // post 좋아요
    @Transactional
    public ApiResponseDto addLikePost(Long postId, UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        // postId와 username을 이용해서 사용자가 이미 Like를 눌렀는지 확인

        //자신의 게시글에 좋아요 X
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));
        if (post.getUser().getUsername().equals(username)) {
            throw new RejectedExecutionException("자신의 게시글에는 '좋아요'를 할 수 없습니다.");
        }
        PostLikedInfo postLikedInfo = postLikedInfoRepository.findByPostIdAndUsername(postId, username).orElse(null);

        if (postLikedInfo == null) {
            postLikedInfo = new PostLikedInfo(postId, username);
            postLikedInfo.setLiked(true);
            postLikedInfoRepository.save(postLikedInfo);
            updatePostLikedCount(postId);
            return new ApiResponseDto("좋아요", 200);
        } else {
            postLikedInfo.setLiked(!postLikedInfo.getLiked());
            postLikedInfoRepository.save(postLikedInfo);
            updatePostLikedCount(postId);
            if (postLikedInfo.getLiked()) {
                return new ApiResponseDto("좋아요", 200);
            } else {
                return new ApiResponseDto("좋아요 취소", 200);
            }
        }
    }

    // count한 like 저장해주기
    private void updatePostLikedCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Integer postLikedCount = postLikedInfoRepository.countByPostIdAndIsLikedIsTrue(postId);
        post.setPostLikedCount(postLikedCount);
    }
}
