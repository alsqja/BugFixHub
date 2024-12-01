package com.example.bugfixhub.repository.comment;

import com.example.bugfixhub.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdAndDeletedFalseOrderByCreatedAtDesc(Long PostId);

    Optional<Comment> findByIdAndDeletedFalse(Long id);

    // 댓글 Id 와 게시글 Id 간의 관계 검증
    boolean existsByIdAndPostId(Long commentId, Long PostId);

}