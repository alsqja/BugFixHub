package com.example.bugfixhub.dto.user;

import com.example.bugfixhub.entity.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDetailResDto {

    private final Long id;
    private final String email;
    private final String name;
    private final int friends;
    private final boolean isFriend;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserDetailResDto(Long id, String email, String name, int friends, boolean isFriend, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.friends = friends;
        this.isFriend = isFriend;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserDetailResDto(User user, Long myId) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.friends = user.getFollowers().stream().filter(f -> f.getStatus().getValue().equals("accepted")).toList().size()
                + user.getFollowings().stream().filter(f -> f.getStatus().getValue().equals("accepted")).toList().size();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();

        for (int i = 0; i < user.getFollowers().size(); i++) {
            if (user.getFollowers().get(i).getId().equals(myId) && user.getFollowers().get(i).getStatus().getValue().equals("accepted")) {
                this.isFriend = true;
                return;
            }
        }

        for (int i = 0; i < user.getFollowings().size(); i++) {
            if (user.getFollowings().get(i).getId().equals(myId) && user.getFollowings().get(i).getStatus().getValue().equals("accepted")) {
                this.isFriend = true;
                return;
            }
        }

        this.isFriend = false;
    }
}
