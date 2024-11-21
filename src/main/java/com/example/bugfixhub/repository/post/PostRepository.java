package com.example.bugfixhub.repository.post;

import com.example.bugfixhub.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface PostRepository extends JpaRepository<Post, Long> {
    default Post findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는(또는 삭제된) 게시글 입니다."));
    }

    Page<Post> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    Page<Post> findByDeletedFalse(Pageable pageable);

    Page<Post> findByTypeAndTitleLikeAndDeletedFalse(String type, String title, Pageable pageable);

    Page<Post> findByTitleLikeAndDeletedFalse(String title, Pageable pageable);

    @Query(value = """
            SELECT 
                p.id AS id,
                p.user_id AS userId,
                u.name AS userName,
                p.title AS title,
                p.type AS type,
                COUNT(c.id) AS comments,
                p.created_at AS createdAt,
                p.updated_at AS updatedAt
            FROM post p
            JOIN user u ON p.user_id = u.id
            LEFT JOIN comment c ON p.id = c.post_id
            WHERE p.user_id IN (
                SELECT f.follow_id
                FROM friend f
                WHERE f.following_id = :userId
                  AND f.status = 'accepted'
            )
            AND p.deleted = false 
            AND (:title IS NULL OR p.title LIKE CONCAT('%', :title, '%'))
            GROUP BY p.id, p.user_id, u.name, p.title, p.type, p.created_at, p.updated_at
            ORDER BY p.created_at DESC
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
    Page<Object[]> findPostsWithPagination(Long userId, String title, Pageable pageable);
}
