package com.example.bugfixhub.controller.comments;

import com.example.bugfixhub.dto.comments.CommentReqDto;
import com.example.bugfixhub.dto.comments.CommentResDto;
import com.example.bugfixhub.dto.user.UserResDto;
import com.example.bugfixhub.service.comment.CommentService;
import com.example.bugfixhub.session.Const;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResDto> createComment(
            @Valid @RequestBody CommentReqDto commentReqDto,
            @SessionAttribute(Const.LOGIN_USER) UserResDto loginUser) {

        Long userId = loginUser.getId();

        CommentResDto commentResDto = commentService.createComment(commentReqDto.getPostId(), userId, commentReqDto);

        return new ResponseEntity<>(commentResDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommentResDto> updateComment(
            @PathVariable Long id,
            @RequestBody CommentReqDto commentReqDto,
            @SessionAttribute(Const.LOGIN_USER) UserResDto loginUser) {

        Long userId = loginUser.getId();

        if (commentReqDto.getContents() == null || commentReqDto.getContents().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정할 내용을 입력해 주세요");
        }

        CommentResDto commentResDto = commentService.updateComment(id, userId, commentReqDto);

        return new ResponseEntity<>(commentResDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @SessionAttribute(Const.LOGIN_USER) UserResDto loginUser) {

        Long userId = loginUser.getId();

        commentService.deleteComment(id, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}