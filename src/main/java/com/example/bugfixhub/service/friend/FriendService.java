package com.example.bugfixhub.service.friend;

import com.example.bugfixhub.entity.friend.Friend;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.repository.friend.FriendRepository;
import com.example.bugfixhub.repository.user.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Getter
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    /**
     * 1. 친구 추가기능 :
     * 사용자 존재 여부 확인 후
     * unChecked 상태로 데이터 전송,
     * follower == following 동일시 친구 추가 불가
     */
    public Friend createFriendRequest(Long follower, Long following) {


        User followerId = userRepository.findByIdOrElseThrow(follower);
        User followingId = userRepository.findByIdOrElseThrow(following);

        boolean requestExists = friendRepository.existsByFollowerAndFollowingAndStatus(followerId,followingId,"unChecked");
        if (requestExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미 전송된 요청입니다.");
        }

        if (follower.equals(following)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인은 친구로 등록할 수 없습니다.");
        }

        Friend friendRequestStatus = new Friend();
        friendRequestStatus.setFollower(followerId);
        friendRequestStatus.setFollowing(followingId);
        friendRequestStatus.setStatus("unChecked");

        return friendRepository.save(friendRequestStatus);
    }

    /**
     * 2. 친구 요청 상태변경 기능(default: unChecked):
     * accepted(수락) 또는 rejected(거절)으로 구분,
     * 사용자 여부 및 중복(거절) 요청 확인
     */
    public Friend updateFriendStatus(Long id, String status) {
        Friend friend = friendRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));


        if (friend.getStatus().equals("accepted")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 전송된 친구 요청입니다.");
        }


        if (friend.getStatus().equals("rejected")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "친구 요청을 거절했습니다.");
        }

        friend.setStatus(status);
        return friendRepository.save(friend);

    }


     /**
     * 3. 친구 요청 전체 확인 기능 :
     * 친구 요청 여부 확인 후 삭제
     */
    public List<Friend> findAllFriendRequestsStatus(Long userId, String status) {
        User user = userRepository.findByIdOrElseThrow(userId);

        return friendRepository.findByFollowerOrFollowingAndStatus(user, user, status);
    }


    /**
     * 4. 친구 삭제 기능 :
     * 친구 요청 여부 확인 후 삭제
     */
    public void deleteFriendRequest(Long id) {

        if (!friendRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 친구 요청입니다.");
        }

        friendRepository.deleteById(id);
    }


}