package com.example.bugfixhub.dto.post;

import com.example.bugfixhub.enums.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostReqDto {
    @NotBlank(message = "제목을 입력해 주세요.")
    private final String title;

    @NotBlank(message = "내용을 입력해 주세요.")
    private final String contents;

    @NotNull(message = "타입을 입력해 주세요.")
    private final PostType type;

    public PostReqDto(String title, String contents, PostType type) {
        this.title = title;
        this.contents = contents;
        this.type = type;
    }
}
