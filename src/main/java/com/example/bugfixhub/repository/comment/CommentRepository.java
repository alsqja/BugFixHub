package com.example.bugfixhub.repository.comment;

import com.example.bugfixhub.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdAndDeletedFalse(Long PostId);

    Comment findByIdAndDeletedFalse(Long id);
}