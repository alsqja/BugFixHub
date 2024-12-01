package com.example.bugfixhub.dto.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostCommentsResDto {
    private final Long id;
    private final Long postId;
    private final Long userId;
    private final String contents;
    private final int likes;
    private final boolean like;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostCommentsResDto(Long id, Long postId, Long userId, String contents, int likes, boolean like, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.contents = contents;
        this.likes = likes;
        this.like = like;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
