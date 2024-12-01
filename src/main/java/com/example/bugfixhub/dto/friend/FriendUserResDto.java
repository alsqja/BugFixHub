package com.example.bugfixhub.dto.friend;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FriendUserResDto {

    private final Long id;
    private final Long followingId;
    private final String userName;
    private final String userEmail;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public FriendUserResDto(Long id, Long followingId, String userName, String userEmail, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.followingId = followingId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
