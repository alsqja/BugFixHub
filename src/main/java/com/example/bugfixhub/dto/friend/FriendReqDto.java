package com.example.bugfixhub.dto.friend;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class FriendReqDto {

    @NotNull(message = "followingId는 필수 값 입니다.")
    private final Long followingId;


    @JsonCreator
    public FriendReqDto(Long followingId) {
        this.followingId = followingId;
    }

}