package com.example.bugfixhub.service.like;

import com.example.bugfixhub.dto.likes.CommentLikeResDto;

public interface CommentLikeService {


    /** 1. 댓글 좋아요
     * @param commentId 댓글 ID
     * @param userId 사용자 ID
     */
    CommentLikeResDto likeComment(Long commentId, Long postId, Long userId);


    /**
     * 2. 댓글 좋아요 취소
     * @param commentId 댓글 ID
     * @param userId    사용자 ID
     */

    void deleteLike(Long commentId, Long postId, Long userId);

}
