package com.example.bugfixhub.dto.comments;

import com.example.bugfixhub.entity.comment.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResDto {
    private Long id;
    private Long userId;
    private Long postId;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentResDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.postId = comment.getPost().getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}