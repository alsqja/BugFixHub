package com.example.bugfixhub.dto.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetAllPostResDataDto {
    private final Long id;
    private final Long userId;
    private final String userName;
    private final String title;
    private final String type;
    private final int comments;
    private final int likes;
    private final boolean like;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public GetAllPostResDataDto(Long id, Long userId, String UserName, String title, String type, int comments, int likes, boolean like, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userName = UserName;
        this.title = title;
        this.type = type;
        this.comments = comments;
        this.likes = likes;
        this.like = like;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
