package com.example.bugfixhub.service.friend;

import com.example.bugfixhub.dto.friend.FriendResDto;
import com.example.bugfixhub.dto.friend.FriendUserResDto;

import java.util.List;

public interface FriendService {
    FriendResDto createFriendRequest(Long followingId, Long id);

    FriendResDto updateFriendStatus(Long id, String status, Long loginUserId);

    void deleteFriendRequest(Long id, Long loginUserId);

    List<FriendUserResDto> findAllFriends(Long id);

    List<FriendUserResDto> findAllFriendReqs(Long id);
}
