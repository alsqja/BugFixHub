package com.example.bugfixhub.repository.post;

import com.example.bugfixhub.entity.post.Post;
import com.example.bugfixhub.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Long> {
    default Post findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 게시글 입니다."));
    }

    Page<Post> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    Page<Post> findByTypeAndTitleLikeAndCreatedAtBetweenAndDeletedFalse(PostType type, String title, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Post> findByTitleLikeAndCreatedAtBetweenAndDeletedFalse(String title, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Post> findByCreatedAtBetweenAndDeletedFalse(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query(value = """
            SELECT p.id AS id,
                   p.user_id AS userId,
                   u.name AS userName,
                   p.title AS title,
                   p.type AS type,
                   COUNT(c.id) AS comments,
                   p.created_at AS createdAt,
                   p.updated_at AS updatedAt,
                   COALESCE(like_count.likes, 0) AS likeCount,
                   CASE WHEN user_like.user_id IS NOT NULL THEN true ELSE false END AS userLiked
            FROM post p
            JOIN user u ON p.user_id = u.id
            LEFT JOIN comment c ON p.id = c.post_id
            LEFT JOIN (
                SELECT post_id, COUNT(*) AS likes
                FROM post_like
                GROUP BY post_id
            ) like_count ON p.id = like_count.post_id
            LEFT JOIN (
                SELECT l.post_id, l.user_id
                FROM post_like l
                WHERE l.user_id = :userId
            ) user_like ON p.id = user_like.post_id
            WHERE (p.user_id IN (
                SELECT f.follow_id
                FROM friend f
                WHERE f.following_id = :userId
                  AND f.status = 'accepted'
            )OR p.user_id IN (
                  SELECT f.following_id
                  FROM friend f
                  WHERE f.follow_id = :userId
                  AND f.status = 'accepted'
              ))
            AND p.deleted = false
            AND (:title IS NULL OR p.title LIKE CONCAT('%', :title, '%'))
            AND p.created_at BETWEEN :start AND :end
            GROUP BY p.id, p.user_id, u.name, p.title, p.type, p.created_at, p.updated_at, like_count.likes, user_like.user_id
            ORDER BY :filter DESC
            """,
            countQuery = """
                    SELECT COUNT(DISTINCT p.id)
                    FROM post p
                    WHERE p.user_id IN (
                        SELECT f.follow_id
                        FROM friend f
                        WHERE f.following_id = :userId
                          AND f.status = 'accepted'
                    )
                    AND p.deleted = false
                    AND (:title IS NULL OR p.title LIKE CONCAT('%', :title, '%'))
                    """,
            nativeQuery = true)
    Page<Object[]> findPostsWithPagination(Long userId, String title, String filter, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
