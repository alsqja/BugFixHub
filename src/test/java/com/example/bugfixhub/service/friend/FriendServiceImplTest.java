package com.example.bugfixhub.service.friend;

import com.example.bugfixhub.dto.friend.FriendUserResDto;
import com.example.bugfixhub.entity.friend.Friend;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.enums.FriendStatus;
import com.example.bugfixhub.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendServiceImpl friendService;

    @Test
    void findAllFriends_success() {
        // given
        Long testUserId = 1L;
        User user = new User(testUserId, "testUser", "test", "password");
        User follower = new User(2L, "followerUser", "follower", "password");
        User following = new User(3L, "followingUser", "following", "password");
        User unAccepted = new User(4L, "upAccepted", "upAccepted", "password");
        User rejected = new User(5L, "rejected", "rejected", "password");

        Friend followerFriend = new Friend(follower, user, FriendStatus.ACCEPTED);
        followerFriend.setTestTimestamps(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        Friend followingFriend = new Friend(user, following, FriendStatus.ACCEPTED);
        followingFriend.setTestTimestamps(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        Friend upAcceptedFriend = new Friend(unAccepted, user, FriendStatus.UNCHECKED);
        upAcceptedFriend.setTestTimestamps(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        Friend rejectedFriend = new Friend(user, rejected, FriendStatus.REJECTED);
        rejectedFriend.setTestTimestamps(LocalDateTime.now().minusDays(1), LocalDateTime.now());

        user.setFollowers(List.of(followerFriend));
        user.setFollowings(List.of(followingFriend));
        when(userRepository.findByIdOrElseThrow(eq(testUserId))).thenReturn(user);

        // when
        List<FriendUserResDto> friends = friendService.findAllFriends(testUserId);

        // then
        assertEquals(2, friends.size());
        assertEquals(follower.getId(), friends.get(0).getFollowingId());
        assertEquals(following.getId(), friends.get(1).getFollowingId());
        verify(userRepository).findByIdOrElseThrow(eq(testUserId));
    }

}