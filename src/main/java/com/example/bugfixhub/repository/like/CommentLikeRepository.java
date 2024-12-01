package com.example.bugfixhub.repository.like;

import com.example.bugfixhub.entity.like.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    /** 좋아요 중복 여부 확인
     */
    boolean existsByCommentIdAndUserId(Long CommentId, Long UserId);

    /** 댓글 좋아요 취소
     */
    void deleteByCommentIdAndUserId(Long CommentId, Long UserId);

}