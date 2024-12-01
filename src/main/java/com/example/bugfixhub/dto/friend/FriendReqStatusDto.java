package com.example.bugfixhub.dto.friend;

import com.example.bugfixhub.enums.FriendStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FriendReqStatusDto {

    @NotNull(message = "status는 필수 값 입니다.")
    private final FriendStatus status;        //상태: unChecked(미확인), accepted(수락), rejected(거절)

    @JsonCreator
    public FriendReqStatusDto(FriendStatus status) {
        this.status = status;
    }
}




