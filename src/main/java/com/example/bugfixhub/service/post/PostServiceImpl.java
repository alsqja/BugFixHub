package com.example.bugfixhub.service.post;

import com.example.bugfixhub.dto.post.GetAllPostResDataDto;
import com.example.bugfixhub.dto.post.GetAllPostResDto;
import com.example.bugfixhub.dto.post.GetIdPostResDto;
import com.example.bugfixhub.dto.post.PostCommentsResDto;
import com.example.bugfixhub.dto.post.PostReqDto;
import com.example.bugfixhub.dto.post.PostResDto;
import com.example.bugfixhub.entity.comment.Comment;
import com.example.bugfixhub.entity.post.Post;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.enums.PostType;
import com.example.bugfixhub.repository.comment.CommentRepository;
import com.example.bugfixhub.repository.post.PostRepository;
import com.example.bugfixhub.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public PostResDto create(Long userId, PostReqDto postReqDto) {
        Post post = new Post(postReqDto.getTitle(), postReqDto.getContents(), postReqDto.getType());
        User user = userRepository.findByIdOrElseThrow(userId);

        post.setUser(user);
        postRepository.save(post);

        return new PostResDto(post);
    }

    @Override
    public GetAllPostResDto getAllPosts(
            String type, String title, int page, int limit, String filter, LocalDateTime startDate, LocalDateTime endDate, Long userId
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(filter).descending());

        Page<Post> postPage = postRepository.findByCreatedAtBetweenAndDeletedFalse(startDate, endDate, pageable);

        if (type != null) {
            if (Objects.equals(type, "follow")) {
                Page<Object[]> resultPage = postRepository.findPostsWithPagination(userId, title, filter, startDate, endDate, pageable);

                List<GetAllPostResDataDto> post = resultPage.stream()
                        .map(row -> new GetAllPostResDataDto(
                                        ((Number) row[0]).longValue(),
                                        ((Number) row[1]).longValue(),
                                        (String) row[2],
                                        (String) row[3],
                                        (String) row[4],
                                        ((Number) row[5]).intValue(),
                                        ((Number) row[8]).intValue(),
                                        !row[9].equals(0),
                                        ((Timestamp) row[6]).toLocalDateTime(),
                                        ((Timestamp) row[7]).toLocalDateTime()
                                )
                        )
                        .toList();

                // 변수 방식이 다르기 때문에 따로 return 처리 진행
                return new GetAllPostResDto(
                        (long) resultPage.getTotalPages(),
                        resultPage.getTotalElements(),
                        post
                );
            } else if (Objects.equals(type, "info") || Objects.equals(type, "ask")) {
                postPage = postRepository.findByTypeAndTitleLikeAndCreatedAtBetweenAndDeletedFalse(PostType.fromValue(type), "%" + (title == null ? "" : title) + "%", startDate, endDate, pageable);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 타입 입니다.");
            }
        } else {
            if (title != null) {
                postPage = postRepository.findByTitleLikeAndCreatedAtBetweenAndDeletedFalse("%" + title + "%", startDate, endDate, pageable);
            }
        }

        Page<GetAllPostResDataDto> posts = postPage.map(post -> {
                    AtomicBoolean isLiked = new AtomicBoolean(false);

                    post.getLikes().forEach(i -> {
                        if (!isLiked.get()) {
                            isLiked.set(i.getUser().getId().equals(userId));
                        }
                    });

                    return new GetAllPostResDataDto(
                            post.getId(),
                            post.getUser().getId(),
                            post.getUser().getName(),
                            post.getTitle(),
                            post.getType().getValue(),
                            post.getComments().size(),
                            post.getLikes().size(),
                            isLiked.get(),
                            post.getCreatedAt(),
                            post.getUpdatedAt()
                    );
                }
        );

        if (posts.getTotalPages() < page + 1 && posts.getTotalElements() > 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "페이지 범위를 벗어났습니다.");
        }

        return new GetAllPostResDto((long) posts.getTotalPages(), posts.getTotalElements(), posts.getContent());
    }

    @Override
    public GetIdPostResDto getPostById(Long id, Long userId) {
        Post post = postRepository.findByIdOrThrow(id);
        User user = userRepository.findByIdOrElseThrow(post.getUser().getId());

        isDelete(post);

        AtomicBoolean isLiked = new AtomicBoolean(false);
        post.getLikes().forEach(i -> {
            if (!isLiked.get()) {
                isLiked.set(i.getUser().getId().equals(userId));
            }
        });

        return new GetIdPostResDto(post.getId(), user.getId(), user.getName(), post.getTitle(), post.getContents(), post.getType().getValue(), post.getLikes().size(), isLiked.get(), post.getCreatedAt(), post.getUpdatedAt());
    }

    @Override
    @Transactional
    public PostResDto update(Long id, Long userId, PostReqDto postReqDto) {
        Post post = postRepository.findByIdOrThrow(id);

        if (!userId.equals(post.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인이 쓴 게시글만 수정할 수 있습니다.");
        }

        isDelete(post);

        post.update(postReqDto.getTitle(), postReqDto.getContents(), postReqDto.getType());

        return new PostResDto(post);
    }

    @Override
    public List<PostCommentsResDto> getPostComments(Long id, Long userId) {
        Post post = postRepository.findByIdOrThrow(id);
        List<Comment> comments = commentRepository.findByPostIdAndDeletedFalseOrderByCreatedAtDesc(id);

        isDelete(post);

        List<PostCommentsResDto> postComments = comments.stream().map(comment -> {
            AtomicBoolean isLiked = new AtomicBoolean(false);

            comment.getLikes().forEach(i -> {
                if (!isLiked.get()) {
                    isLiked.set(i.getUser().getId().equals(userId));
                }
            });

            return new PostCommentsResDto(
                    comment.getId(),
                    comment.getPost().getId(),
                    comment.getUser().getId(),
                    comment.getContents(),
                    comment.getLikes().size(),
                    isLiked.get(),
                    comment.getCreatedAt(),
                    comment.getUpdatedAt()
            );
        }).toList();

        return postComments;
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        Post findPost = postRepository.findByIdOrThrow(id);
        User user = userRepository.findByIdOrElseThrow(userId);

        isDelete(findPost);

        if (!user.getId().equals(findPost.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인이 쓴 게시글만 삭제할 수 있습니다.");
        }

        postRepository.delete(findPost);
    }

    /**
     * 게시글 삭제 확인
     * delete true 일 경우 삭제된 게시글로 처리
     */
    private void isDelete(Post post) {
        if (post.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 게시글 입니다.");
        }
    }
}
