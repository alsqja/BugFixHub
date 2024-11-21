package com.example.bugfixhub.service.post;

import com.example.bugfixhub.dto.post.*;
import com.example.bugfixhub.entity.post.Post;
import com.example.bugfixhub.entity.user.User;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostResDto create(Long userId, PostReqDto postReqDto) {
        Post post = new Post(postReqDto.getTitle(), postReqDto.getContents(), postReqDto.getType());
        User user = userRepository.findByIdOrElseThrow(userId);

        if (post.getTitle() == null || post.getContents() == null || post.getType() == null) {
            String errorMessage = inputErrorMessage(post);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage + "을 입력해 주세요.");
        }

        if (!(post.getType().equals("info") || post.getType().equals("ask"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 타입입니다.");
        }

        post.setUser(user);
        postRepository.save(post);

        return new PostResDto(post);
    }

    @Override
    public GetAllPostResDto getAllPosts(String type, String title, int page, int limit, Long userId) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        Page<Post> postPage = postRepository.findByDeletedFalse(pageable);

        if (type != null) {
            if (Objects.equals(type, "follow")) {
                Page<Object[]> resultPage = postRepository.findPostsWithPagination(userId, title, pageable);

                List<GetAllPostResDataDto> post = resultPage.stream()
                        .map(row -> new GetAllPostResDataDto(
                                ((Number) row[0]).longValue(),
                                ((Number) row[1]).longValue(),
                                (String) row[2],
                                (String) row[3],
                                (String) row[4],
                                ((Number) row[5]).intValue(),
                                ((Timestamp) row[6]).toLocalDateTime(),
                                ((Timestamp) row[7]).toLocalDateTime()
                        ))
                        .toList();

                // 변수 방식이 다르기 때문에 따로 return 처리 진행
                return new GetAllPostResDto(
                        (long) resultPage.getTotalPages(),
                        resultPage.getTotalElements(),
                        post
                );

            } else if (Objects.equals(type, "info") || Objects.equals(type, "ask")) {
                postPage = postRepository.findByTypeAndTitleLikeAndDeletedFalse(type, "%" + (title == null ? "" : title) + "%", pageable);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "잘못된 타입입니다.");
            }
        } else {
            if (title != null) {
                postPage = postRepository.findByTitleLikeAndDeletedFalse("%" + title + "%", pageable);
            }
        }

        Page<GetAllPostResDataDto> posts = postPage.map(post -> new GetAllPostResDataDto(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getType(),
                post.getComments().size(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        ));

        if (posts.getTotalPages() < page + 1 && posts.getTotalElements() > 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "페이지 범위를 벗어났습니다.");
        }

        return new GetAllPostResDto((long) posts.getTotalPages(), posts.getTotalElements(), posts.getContent());
    }

    @Override
    public GetIdPostResDto getPostById(Long id) {
        Post post = postRepository.findByIdOrThrow(id);
        User user = userRepository.findByIdOrElseThrow(post.getUser().getId());

        isDelete(post);

        return new GetIdPostResDto(post.getId(), user.getId(), user.getName(), post.getTitle(), post.getContents(), post.getType(), post.getCreatedAt(), post.getUpdatedAt());
    }

    @Override
    @Transactional
    public PostResDto update(Long id, Long userId, PostReqDto postReqDto) {
        Post post = postRepository.findByIdOrThrow(id);

        if (!userId.equals(post.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인이 쓴 게시글만 수정할 수 있습니다.");
        }

        isDelete(post);

        if (postReqDto.getType() != null) {
            if (
                    !(Objects.equals(postReqDto.getType(), "info") ||
                            Objects.equals(postReqDto.getType(), "ask"))
            ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 타입입니다.");
            }
        }

        post.update(postReqDto.getTitle(), postReqDto.getContents(), postReqDto.getType());

        return new PostResDto(post);
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

        findPost.updateDelete(true);
    }

    /**
     * 게시글 등록시 예외 처리 메시지 출력 로직
     *
     * @param post : title, contents, type 사용
     * @return : 입력 되지 않은 변수 이름 저장하여 반환
     */
    private static String inputErrorMessage(Post post) {
        Map<String, String> nullCheckMap = new HashMap<>();
        String errorMessage = "";

        nullCheckMap.put("제목", post.getTitle());
        nullCheckMap.put("내용", post.getContents());
        nullCheckMap.put("타입", post.getType());

        for (String key : nullCheckMap.keySet()) {
            if (nullCheckMap.get(key) == null) {
                if (!errorMessage.isEmpty()) {
                    errorMessage += ", ";
                }

                errorMessage += key;
            }
        }
        return errorMessage;
    }

    /**
     * 게시글 삭제 확인
     * delete true 일 경우 삭제된 게시글로 처리
     */
    private void isDelete(Post post) {
        if (post.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "없는(또는 삭제된) 게시글 입니다.");
        }
    }

    private void isUSerPost(User user) {

    }
}
