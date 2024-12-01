package com.example.bugfixhub.dto.post;

import com.example.bugfixhub.entity.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResDto {
    private final Long id;
    private final Long userId;
    private final String title;
    private final String contents;
    private final String type;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostResDto(Post post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.type = post.getType().getValue();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
