package com.example.bugfixhub.repository.friend;

import com.example.bugfixhub.entity.friend.Friend;
import com.example.bugfixhub.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    /**
     * 친구 추가시 중복 추가 요청 확인
     */
//    boolean existsByFollowerAndFollowingAndStatus(User follower, User following, String status);

    Optional<Friend> findByFollowerAndFollowing(User follower, User following);

    /**
     * 친구 요청 상태 전체 확인
     */
    List<Friend> findByFollowerOrFollowingAndStatus(User follower, User following, String status);


    List<Friend> findByFollowerAndStatus(User follower, String status);
}
