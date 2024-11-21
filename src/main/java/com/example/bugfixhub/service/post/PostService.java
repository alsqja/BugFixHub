package com.example.bugfixhub.service.post;

import com.example.bugfixhub.dto.post.*;

public interface PostService {
    PostResDto create(Long userId, PostReqDto postReqDto);

    GetAllPostResDto getAllPosts(String type, String title, int page, int limit, Long userId);

    GetIdPostResDto getPostById(Long id);

    PostResDto update(Long id, Long userId, PostReqDto postReqDto);

    void delete(Long id, Long userId);
}
