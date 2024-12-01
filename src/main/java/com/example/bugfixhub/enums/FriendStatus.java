package com.example.bugfixhub.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum FriendStatus {
    ACCEPTED("accepted"),
    UNCHECKED("unChecked"),
    REJECTED("rejected");

    private final String value;

    FriendStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static FriendStatus fromValue(String value) {
        for (FriendStatus type : FriendStatus.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "타입을 확인해 주세요.");
    }
}
