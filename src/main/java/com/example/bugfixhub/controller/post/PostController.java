package com.example.bugfixhub.controller.post;

import com.example.bugfixhub.dto.post.GetAllPostResDto;
import com.example.bugfixhub.dto.post.GetIdPostResDto;
import com.example.bugfixhub.dto.post.PostCommentsResDto;
import com.example.bugfixhub.dto.post.PostReqDto;
import com.example.bugfixhub.dto.post.PostResDto;
import com.example.bugfixhub.dto.user.UserResDto;
import com.example.bugfixhub.service.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * 게시글 등록
     */
    @PostMapping
    public ResponseEntity<PostResDto> create(
            @Valid @RequestBody PostReqDto postReqDto,
            @SessionAttribute UserResDto loginUser
    ) {
        PostResDto postResDto = postService.create(loginUser.getId(), postReqDto);

        return new ResponseEntity<>(postResDto, HttpStatus.CREATED);
    }

    /**
     * 전체 게시글 조회
     *
     * @param type      : [follow || info || ask]
     *                  follow : 친구 게시글만 받아오기
     *                  info : 정보 공유 글만 받아오기
     *                  ask : 질문 글만 받아오기
     * @param title     : 제목 검색
     * @param page      : 페이지 수 (default = 1 / size = 1 ~ totalPage)
     * @param limit     : 한 페이지 게시 글 수 (default = 10)
     * @param filter    : [createdAt(default), updatedAt, likes]
     *                  createdAt : 생성일자 순으로 나열
     *                  updatedAt : 수정일 순으로 나열
     *                  like : 좋아요 많은 순으로 나열
     * @param startDate : 검색 시작 일자
     * @param endDate   : 검색 끝 일자(default : now)
     */
    @GetMapping
    public ResponseEntity<GetAllPostResDto> getAllPosts(
            String type, String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String filter,
            @RequestParam(required = false, defaultValue = "1900-01-01T00:00:00") String startDate,
            @RequestParam(required = false) String endDate,
            @SessionAttribute UserResDto loginUser
    ) {
        if (!(Objects.equals(filter, "createdAt") || Objects.equals(filter, "updatedAt") || Objects.equals(filter, "likes"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 타입입니다.");
        }

        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = endDate == null ? LocalDateTime.now() : LocalDateTime.parse(endDate);

        GetAllPostResDto getAllPostResDto =
                postService.getAllPosts(
                        type, title, page - 1, limit, filter, start, end, loginUser.getId()
                );

        return new ResponseEntity<>(getAllPostResDto, HttpStatus.OK);
    }

    /**
     * 게시글 수정
     */
    @PatchMapping("/{id}")
    public ResponseEntity<PostResDto> update(
            @PathVariable Long id,
            @RequestBody PostReqDto postReqDto,
            @SessionAttribute UserResDto loginUser
    ) {
        PostResDto post = postService.update(id, loginUser.getId(), postReqDto);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }


    /**
     * 선택 게시글 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<GetIdPostResDto> getPostById(
            @PathVariable Long id,
            @SessionAttribute UserResDto loginUser
    ) {
        GetIdPostResDto postResDto = postService.getPostById(id, loginUser.getId());

        return new ResponseEntity<>(postResDto, HttpStatus.OK);
    }

    /**
     * 게시글 별 댓글 조회
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<PostCommentsResDto>> getPostComments(
            @PathVariable Long id,
            @SessionAttribute UserResDto loginUser
    ) {
        List<PostCommentsResDto> postCommentsResDto = postService.getPostComments(id, loginUser.getId());

        return new ResponseEntity<>(postCommentsResDto, HttpStatus.OK);
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<PostResDto> delete(
            @PathVariable Long id,
            @SessionAttribute UserResDto loginUser
    ) {
        postService.delete(id, loginUser.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
