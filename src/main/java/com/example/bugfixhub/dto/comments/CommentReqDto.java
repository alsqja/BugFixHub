package com.example.bugfixhub.dto.comments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentReqDto {

    @NotBlank(message = "댓글 내용을 입력해 주세요")
    private String contents;

    @NotNull(message = "게시물 번호를 입력해 주세요.")
    private Long postId;
}