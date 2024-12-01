package com.example.bugfixhub.service.friend;

import com.example.bugfixhub.dto.friend.FriendResDto;
import com.example.bugfixhub.dto.friend.FriendUserResDto;
import com.example.bugfixhub.entity.friend.Friend;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.enums.FriendStatus;
import com.example.bugfixhub.repository.friend.FriendRepository;
import com.example.bugfixhub.repository.user.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    /**
     * 1. 친구 추가기능 :
     * 사용자 존재 여부 확인 후
     * unChecked 상태로 데이터 전송,
     * follower == following 동일시 친구 추가 불가
     */
    public FriendResDto createFriendRequest(Long follower, Long following) {

        if (follower.equals(following)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인은 친구로 등록할 수 없습니다.");
        }

        User followerId = userRepository.findByIdOrElseThrow(follower);
        User followingId = userRepository.findByIdOrElseThrow(following);

        FriendResDto friend = handleExistFriend(following, follower);
        if (friend != null) {
            return friend;
        }

        FriendResDto friend2 = handleExistFriend(follower, following);
        if (friend2 != null) {
            return friend2;
        }

        Friend friendRequestStatus = new Friend();
        friendRequestStatus.setFollower(followerId);
        friendRequestStatus.setFollowing(followingId);
        friendRequestStatus.setStatus(FriendStatus.fromValue("unChecked"));

        return new FriendResDto(friendRepository.save(friendRequestStatus));
    }

    private FriendResDto handleExistFriend(Long follower, Long following) {
        User followingUser = userRepository.findByIdOrElseThrow(following);
        User followUser = userRepository.findByIdOrElseThrow(follower);
        Optional<Friend> findFriend = friendRepository.findByFollowerAndFollowing(followUser, followingUser);

        if (findFriend.isPresent()) {
            String status = findFriend.get().getStatus().getValue();
            if (status.equals("unChecked")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 전송된 요청 입니다.");
            }
            if (status.equals("accepted")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 추가된 사용자 입니다.");
            }

            Friend friend = findFriend.get();
            friend.setStatus(FriendStatus.fromValue("unChecked"));
            return new FriendResDto(friend);
        }
        return null;
    }

    /**
     * 2. 친구 요청 상태변경 기능(default: unChecked):
     * accepted(수락) 또는 rejected(거절)으로 구분,
     * 사용자 여부 및 중복(거절) 요청 확인
     */
    @Transactional
    public FriendResDto updateFriendStatus(Long id, String status, Long loginUserId) {
        Friend friend = friendRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 요청 입니다."));

        if (!friend.getFollower().getId().equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인에게 온 요청만 처리할 수 있습니다.");
        }
        if (friend.getStatus().getValue().equals("accepted")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 처리된 요청 입니다.");
        }

        friend.setStatus(FriendStatus.fromValue(status));
        return new FriendResDto(friend);
    }


    /**
     * 3. 친구 요청 전체 확인 기능 :
     * findAllFriends -> 친구 목록 전체 조회
     * findAllFriendReqs -> 친구 요청 전체 조회
     */
    @Override
    public List<FriendUserResDto> findAllFriends(Long id) {
        User user = userRepository.findByIdOrElseThrow(id);

        List<FriendUserResDto> followUsers = user.getFollowers().stream()
                .filter(i -> i.getStatus().getValue().equals("accepted"))
                .map(i -> {
                    User friendUser = Objects.equals(i.getFollower().getId(), id) ? i.getFollowing() : i.getFollower();

                    return new FriendUserResDto(
                            i.getId(),
                            friendUser.getId(),
                            friendUser.getName(),
                            friendUser.getEmail(),
                            i.getCreatedAt(),
                            i.getUpdatedAt()
                    );
                }).toList();

        List<FriendUserResDto> followingUsers = user.getFollowings().stream()
                .filter(i -> i.getStatus().getValue().equals("accepted"))
                .map(i -> {
                    User friendUser = Objects.equals(i.getFollower().getId(), id) ? i.getFollowing() : i.getFollower();

                    return new FriendUserResDto(
                            i.getId(),
                            friendUser.getId(),
                            friendUser.getName(),
                            friendUser.getEmail(),
                            i.getCreatedAt(),
                            i.getUpdatedAt()
                    );
                }).toList();

        List<FriendUserResDto> friends = new ArrayList<>();
        friends.addAll(followingUsers);
        friends.addAll(followUsers);

        friends.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));

        return friends;
    }

    @Override
    public List<FriendUserResDto> findAllFriendReqs(Long id) {
        User user = userRepository.findByIdOrElseThrow(id);
        List<Friend> requests = friendRepository.findByFollowerAndStatus(user, FriendStatus.fromValue("unChecked"));
        return requests.stream()
                .filter(i -> i.getStatus().getValue().equals("unChecked"))
                .map(i -> {
                    User friendUser = Objects.equals(i.getFollower().getId(), id) ? i.getFollowing() : i.getFollower();

                    return new FriendUserResDto(
                            i.getId(),
                            friendUser.getId(),
                            friendUser.getName(),
                            friendUser.getEmail(),
                            i.getCreatedAt(),
                            i.getUpdatedAt()
                    );
                }).toList();
    }

    /**
     * 4. 친구 삭제 기능 :
     * 친구 요청 여부 확인 후 삭제
     */
    public void deleteFriendRequest(Long id, Long loginUserId) {
        Friend friend = friendRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 요청 입니다."));

        if (!loginUserId.equals(friend.getFollower().getId())) {
            if (friend.getStatus().getValue().equals("accepted") && loginUserId.equals(friend.getFollowing().getId())) {
                friendRepository.deleteById(id);
                return;
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인의 요청만 삭제할 수 있습니다.");
        }
        friendRepository.deleteById(id);
    }


}