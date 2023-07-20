package com.example.springbackoffice.service;

import com.example.springbackoffice.dto.ApiResponseDto;
import com.example.springbackoffice.dto.CommentRequestDto;
import com.example.springbackoffice.dto.CommentResponseDto;
import com.example.springbackoffice.entity.Comment;
import com.example.springbackoffice.entity.CommentLikedInfo;
import com.example.springbackoffice.entity.Post;
import com.example.springbackoffice.entity.User;
import com.example.springbackoffice.repository.CommentLikedInfoRepository;
import com.example.springbackoffice.repository.CommentRepository;
import com.example.springbackoffice.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostService postService;
    private final CommentRepository commentRepository;
    private final CommentLikedInfoRepository commentLikedInfoRepository;

    // 댓글 생성
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long id, User user){ //게시글과 유저 정보 파라미터
        Post post = postService.findPost(id); // 게시글 존재하면 가져오기

        Comment comment = new Comment(commentRequestDto,user,post); // comment 본문

        commentRepository.save(comment);


        return new CommentResponseDto(comment);

    }

    @Transactional //댓글 수정
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {

        Comment comment = commentRepository.findById(id).orElseThrow();

        //작성자 확인
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RejectedExecutionException();
        }

        comment.setContents(requestDto.getContents());

        return new CommentResponseDto(comment);
    }

    //댓글 삭제
    public void deleteComment(Long id, User user) {

        Comment comment = commentRepository.findById(id).orElseThrow();

        // 요청자가 운영자 이거나 댓글 작성자(post.user) 와 요청자(user) 가 같은지 체크
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RejectedExecutionException();
        }

        commentRepository.delete(comment);
    }
    //댓글 하나 조회(댓글 수정을 위해)
    public Comment findById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found" + id));
    }

    public List<Comment> findAll(){
        return commentRepository.findAll();
    }

    // comment 좋아요
    @Transactional
    public ApiResponseDto addLikeComment(Long commentId, UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();

        // 존재하는 댓글인지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));

        //자신의 댓글에 좋아요 X
        if (comment.getUser().getId().equals(userId)) {
            throw new RejectedExecutionException("자신의 댓글에는 '좋아요'를 할 수 없습니다.");
        }
        CommentLikedInfo commentLikedInfo = commentLikedInfoRepository.findByCommentIdAndUserId(commentId, userId).orElse(null);

        if (commentLikedInfo == null) {
            commentLikedInfo = new CommentLikedInfo(commentId, userId);
            commentLikedInfo.setLiked(true);
            commentLikedInfoRepository.save(commentLikedInfo);
            updateCommentLikedCount(commentId);
            return new ApiResponseDto(200, "좋아요");
        } else {
            commentLikedInfo.setLiked(!commentLikedInfo.getLiked());
            commentLikedInfoRepository.save(commentLikedInfo);
            updateCommentLikedCount(commentId);
            if (commentLikedInfo.getLiked()) {
                return new ApiResponseDto(200, "좋아요");
            } else {
                return new ApiResponseDto(200, "좋아요 취소");
            }
        }
    }

    // count한 like 저장해주기
    private void updateCommentLikedCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Integer commentLikedCount = commentLikedInfoRepository.countByCommentIdAndLikedIsTrue(commentId);
        comment.setCommentLikedCount(commentLikedCount);
    }
}
