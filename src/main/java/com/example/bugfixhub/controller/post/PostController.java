package com.example.bugfixhub.controller.post;

import com.example.bugfixhub.dto.post.GetAllPostResDto;
import com.example.bugfixhub.dto.post.GetIdPostResDto;
import com.example.bugfixhub.dto.post.PostReqDto;
import com.example.bugfixhub.dto.post.PostResDto;
import com.example.bugfixhub.dto.user.UserResDto;
import com.example.bugfixhub.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody PostReqDto postReqDto,
            @SessionAttribute UserResDto loginUser
    ) {
        PostResDto postResDto = postService.create(loginUser.getId(), postReqDto);

        return new ResponseEntity<>(postResDto, HttpStatus.CREATED);
    }

    /**
     * 전체 게시글 조회
     *
     * @param type  : [follow || info || ask]
     *              follow : 친구 게시글만 받아오기
     *              info : 정보 공유 글만 받아오기
     *              ask : 질문 글만 받아오기
     * @param title : 제목 검색
     * @param page  : 페이지 수 (default = 1 / size = 1 ~ totalPage)
     * @param limit : 한 페이지 게시 글 수 (default = 10)
     */
    @GetMapping
    public ResponseEntity<GetAllPostResDto> getAllPosts(
            String type, String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @SessionAttribute UserResDto loginUser
    ) {
        GetAllPostResDto getAllPostResDto = postService.getAllPosts(type, title, page - 1, limit, loginUser.getId());

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
            @PathVariable Long id
    ) {
        GetIdPostResDto postResDto = postService.getPostById(id);

        return new ResponseEntity<>(postResDto, HttpStatus.OK);
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
