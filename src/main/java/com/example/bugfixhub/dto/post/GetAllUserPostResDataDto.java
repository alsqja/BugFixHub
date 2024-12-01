package com.example.bugfixhub.dto.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetAllUserPostResDataDto {
    private final Long id;
    private final Long userId;
    private final String userName;
    private final String title;
    private final String type;
    private final int comments;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public GetAllUserPostResDataDto(Long id, Long userId, String userName, String title, String type, int comments, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.type = type;
        this.comments = comments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
