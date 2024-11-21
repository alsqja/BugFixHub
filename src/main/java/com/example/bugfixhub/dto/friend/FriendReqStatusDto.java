package com.example.bugfixhub.dto.friend;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendReqStatusDto {

    @NotNull(message = "status는 필수 값 입니다.")
    private String status;        //상태: unChecked(미확인), accepted(수락), rejected(거절)

}




