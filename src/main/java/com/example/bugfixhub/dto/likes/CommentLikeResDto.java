package com.example.bugfixhub.dto.likes;

import com.example.bugfixhub.entity.like.CommentLike;
import lombok.*;

@Getter
public class CommentLikeResDto {


    private final Long id;

    private final Long userId;

    private final Long commentId;


    public CommentLikeResDto(CommentLike commentLike) {
        this.id = commentLike.getId();
        this.userId = commentLike.getUser().getId();
        this.commentId = commentLike.getComment().getId();
    }

}
