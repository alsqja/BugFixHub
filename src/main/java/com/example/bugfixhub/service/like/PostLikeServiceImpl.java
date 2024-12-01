package com.example.bugfixhub.service.like;

import com.example.bugfixhub.dto.likes.PostLikeResDto;
import com.example.bugfixhub.entity.like.PostLike;
import com.example.bugfixhub.entity.post.Post;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.repository.like.PostLikeRepository;
import com.example.bugfixhub.repository.post.PostRepository;
import com.example.bugfixhub.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public PostLikeResDto addPostLike(Long postId, Long userId) {

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 게시물입니다.")
        );

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (post.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인 게시글에는 좋아요를 남길 수 없습니다");
        }

        if (post.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 게시글입니다.");
        }

        PostLike existingLike = postLikeRepository.findByPostAndUser(post, user).orElse(null);

        if (existingLike != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요를 눌렀습니다.");
        }

        PostLike newPostLike = new PostLike(user, post);
        postLikeRepository.save(newPostLike);
        return new PostLikeResDto(newPostLike);
    }

    @Transactional
    @Override
    public void canclePostLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 게시물입니다.")
        );

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
        );

        if (post.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 게시글입니다.");
        }

        PostLike existingLike = postLikeRepository.findByPostAndUser(post, user).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "좋아요가 존재하지 않습니다.")
        );

        postLikeRepository.delete(existingLike);
    }
}