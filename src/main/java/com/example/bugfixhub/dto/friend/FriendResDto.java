package com.example.bugfixhub.dto.friend;

import com.example.bugfixhub.entity.friend.Friend;
import lombok.Getter;

@Getter
public class FriendResDto {

    private final Long id;
    private final Long followId;
    private final Long followingId;
    private final String status;

    public FriendResDto(Long id, Long followerId, Long followingId, String status) {
        this.id = id;
        this.followId = followerId;
        this.followingId = followingId;
        this.status = status;
    }

    public FriendResDto(Friend friend) {
        this.id = friend.getId();
        this.followId = friend.getFollower().getId();
        this.followingId = friend.getFollowing().getId();
        this.status = friend.getStatus().getValue();
    }
}