package com.example.bugfixhub.repository.like;

import com.example.bugfixhub.entity.like.PostLike;
import com.example.bugfixhub.entity.post.Post;
import com.example.bugfixhub.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);
}