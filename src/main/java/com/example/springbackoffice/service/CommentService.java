package com.example.springbackoffice.service;

import com.example.springbackoffice.dto.ApiResponseDto;
import com.example.springbackoffice.entity.CommentLikedInfo;
import com.example.springbackoffice.repository.CommentLikedInfoRepository;
import com.example.springbackoffice.repository.CommentRepository;
import com.example.springbackoffice.repository.PostLikedInfoRepository;
import com.example.springbackoffice.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.RejectedExecutionException;
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final CommentLikedInfoRepository commentLikedInfoRepository;

    public CommentService(CommentRepository commentRepository, CommentLikedInfoRepository commentLikedInfoRepository) {
        this.commentRepository = commentRepository;
        this.commentLikedInfoRepository = commentLikedInfoRepository;
    }
    // comment 좋아요
    @Transactional
    public ApiResponseDto addLikeComment(Long commentId, UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        // commentId와 username을 이용해서 사용자가 이미 Like를 눌렀는지 확인

        //자신의 게시글에 좋아요 X
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));
        if (comment.getUser().getUsername().equals(username)) {
            throw new RejectedExecutionException("자신의 댓글에는 '좋아요'를 할 수 없습니다.");
        }
        CommentLikedInfo commentLikedInfo = commentLikedInfoRepository.findByCommentIdAndUsername(commentId, username).orElse(null);

        if (commentLikedInfo == null) {
            commentLikedInfo = new CommentLikedInfo(commentId, username);
            commentLikedInfo.setLiked(true);
            commentLikedInfoRepository.save(commentLikedInfo);
            updateCommentLikedCount(commentId);
            return new ApiResponseDto("좋아요", 200);
        } else {
            commentLikedInfo.setLiked(!commentLikedInfo.getLiked());
            commentLikedInfoRepository.save(commentLikedInfo);
            updateCommentLikedCount(commentId);
            if (commentLikedInfo.getLiked()) {
                return new ApiResponseDto("좋아요", 200);
            } else {
                return new ApiResponseDto("좋아요 취소", 200);
            }
        }
    }

    // count한 like 저장해주기
    private void updateCommentLikedCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Integer commentLikedCount = commentLikedInfoRepository.countByCommentIdAndIsLikedIsTrue(commentId);
        comment.setCommentLikedCount(commentLikedCount);
    }
}
