package com.example.bugfixhub.dto.post;

import lombok.Getter;

import java.util.List;

@Getter
public class GetAllUserPostResDto {
    private final Long totalPages;
    private final Long totalElements;
    private final List<GetAllPostResDataDto> data;

    public GetAllUserPostResDto(Long totalPages, Long totalElements, List<GetAllPostResDataDto> getAllUserPostResDto) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.data = getAllUserPostResDto;
    }
}
