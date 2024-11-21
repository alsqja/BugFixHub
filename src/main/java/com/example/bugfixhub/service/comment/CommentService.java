package com.example.bugfixhub.service.comment;

import com.example.bugfixhub.dto.comments.CommentReqDto;
import com.example.bugfixhub.dto.comments.CommentResDto;

public interface CommentService {

    CommentResDto createComment(Long postId, Long userId, CommentReqDto commentReqDto);

    CommentResDto updateComment(Long commentId, Long userId, CommentReqDto dto);

    void deleteComment(Long commentId, Long userId);
}