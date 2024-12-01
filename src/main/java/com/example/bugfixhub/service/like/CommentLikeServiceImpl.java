package com.example.bugfixhub.service.like;

import com.example.bugfixhub.dto.likes.CommentLikeResDto;
import com.example.bugfixhub.entity.comment.Comment;
import com.example.bugfixhub.entity.like.CommentLike;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.repository.comment.CommentRepository;
import com.example.bugfixhub.repository.like.CommentLikeRepository;
import com.example.bugfixhub.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentLikeServiceImpl(CommentLikeRepository commentLikeRepository,
                                  CommentRepository commentRepository,
                                  UserRepository userRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    /**
     * 0. 게시글과 댓글 관계 검증(추가)
     */
    private void validateCommentAndPostRelation(Long commentId, Long postId) {
        if (!commentRepository.existsByIdAndPostId(commentId, postId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 ID와 댓글 ID의 관계가 유효하지 않습니다.");
        }
    }


    /**
     * 1. 댓글 좋아요:
     * 댓글 및 본인 여부 확인 -> 중복 확인 -> 좋아요
     */
    @Override
    @Transactional
    public CommentLikeResDto likeComment(Long commentId, Long postId, Long userId) {


        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        // 게시글과 댓글 관계 검증
        validateCommentAndPostRelation(commentId, postId);

        if (comment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인 댓글에는 좋아요를 남길 수 없습니다");
        }


        if (commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요를 남긴 댓글입니다.");
        }


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));



        CommentLike commentLike = new CommentLike(user, comment);
        commentLikeRepository.save(commentLike);

        return new CommentLikeResDto(commentLike);

    }


    /**
     * 2. 댓글 좋아요 취소
     */
    @Override
    @Transactional
    public void deleteLike(Long commentId, Long postId, Long userId) {

        if (!commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "좋아요를 남기지 않은 댓글입니다.");
        }

        commentLikeRepository.deleteByCommentIdAndUserId(commentId, userId);

    }

}