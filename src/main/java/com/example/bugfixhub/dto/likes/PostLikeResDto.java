package com.example.bugfixhub.dto.likes;

import com.example.bugfixhub.entity.like.PostLike;
import lombok.*;

@Getter
public class PostLikeResDto {


    private final Long id;

    private final Long userId;

    private final Long postId;


    public PostLikeResDto(PostLike postLike) {
        this.id = postLike.getId();
        this.userId = postLike.getUser().getId();
        this.postId = postLike.getPost().getId();
    }

}
