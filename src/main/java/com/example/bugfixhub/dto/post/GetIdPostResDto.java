package com.example.bugfixhub.dto.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetIdPostResDto {
    private final Long id;
    private final Long userId;
    private final String userName;
    private final String title;
    private final String contents;
    private final String type;
    private final int likes;
    private final boolean like;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public GetIdPostResDto(Long id, Long userId, String userName, String title, String contents, String type, int likes, boolean like, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.contents = contents;
        this.type = type;
        this.likes = likes;
        this.like = like;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
