package com.example.bugfixhub.config;

import com.example.bugfixhub.entity.comment.Comment;
import com.example.bugfixhub.entity.friend.Friend;
import com.example.bugfixhub.entity.like.CommentLike;
import com.example.bugfixhub.entity.like.PostLike;
import com.example.bugfixhub.entity.post.Post;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.enums.FriendStatus;
import com.example.bugfixhub.enums.PostType;
import com.example.bugfixhub.repository.comment.CommentRepository;
import com.example.bugfixhub.repository.friend.FriendRepository;
import com.example.bugfixhub.repository.like.CommentLikeRepository;
import com.example.bugfixhub.repository.like.PostLikeRepository;
import com.example.bugfixhub.repository.post.PostRepository;
import com.example.bugfixhub.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Component
@Profile("dev")
public class DataInitializer {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final FriendRepository friendRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        for (int i = 1; i <= 30; i++) {
            User user = new User("name" + i, "email" + i + "@email.com", passwordEncoder.encode("Alsqja@0000"));
            userRepository.save(user);
        }

        for (int i = 1; i <= 30; i++) {
            int random = (int) (Math.random() * 29) + 1;

            PostType[] postTypes = new PostType[2];
            postTypes[0] = PostType.fromValue("info");
            postTypes[1] = PostType.fromValue("ask");
            int randomType = (int) (Math.random() * 2);

            Post post = new Post("title" + i, "contents" + i, postTypes[randomType]);
            post.setUser(userRepository.findByIdOrElseThrow((long) random));

            postRepository.save(post);
        }

        for (int i = 1; i <= 30; i++) {
            Long random = (long) (int) (Math.random() * 29) + 1;
            Long randomPost = (long) (int) (Math.random() * 29) + 1;

            Comment comment = new Comment("contents" + i, userRepository.findByIdOrElseThrow(random), postRepository.findByIdOrThrow(randomPost));

            commentRepository.save(comment);
        }

        for (int i = 1; i <= 30; i++) {
            Long random = (long) (int) (Math.random() * 29) + 1;
            Long randomTo = (long) (int) (Math.random() * 29) + 1;

            while (randomTo.equals(random)) {
                random = (long) (int) (Math.random() * 29) + 1;
            }

            FriendStatus[] friendStatuses = new FriendStatus[3];
            friendStatuses[0] = FriendStatus.fromValue("accepted");
            friendStatuses[1] = FriendStatus.fromValue("rejected");
            friendStatuses[2] = FriendStatus.fromValue("unChecked");
            int randomType = (int) (Math.random() * 3);

            Friend friend = new Friend(userRepository.findByIdOrElseThrow(random), userRepository.findByIdOrElseThrow(randomTo), friendStatuses[randomType]);

            friendRepository.save(friend);
        }

        for (int i = 0; i < 100; i++) {
            Long random = (long) (int) (Math.random() * 29) + 1;
            Long randomPost = (long) (int) (Math.random() * 29) + 1;

            PostLike postLike = new PostLike();
            postLike.setUser(userRepository.findByIdOrElseThrow(random));
            postLike.setPost(postRepository.findByIdOrThrow(randomPost));

            postLikeRepository.save(postLike);
        }

        for (int i = 0; i < 100; i++) {
            Long random = (long) (int) (Math.random() * 29) + 1;
            Long randomPost = (long) (int) (Math.random() * 29) + 1;

            CommentLike commentLike = new CommentLike();
            commentLike.setUser(userRepository.findByIdOrElseThrow(random));
            commentLike.setComment(commentRepository.findById(randomPost).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

            commentLikeRepository.save(commentLike);
        }
    }
}
