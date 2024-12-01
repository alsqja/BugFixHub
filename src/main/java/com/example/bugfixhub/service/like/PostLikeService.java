package com.example.bugfixhub.service.like;

import com.example.bugfixhub.dto.likes.PostLikeResDto;

public interface PostLikeService {
    PostLikeResDto addPostLike(Long postId, Long userId);
    void canclePostLike(Long postId, Long userId);
}