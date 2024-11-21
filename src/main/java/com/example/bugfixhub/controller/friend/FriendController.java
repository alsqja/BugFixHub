package com.example.bugfixhub.controller.friend;

import com.example.bugfixhub.dto.friend.FriendReqDto;
import com.example.bugfixhub.dto.friend.FriendReqStatusDto;
import com.example.bugfixhub.dto.friend.FriendResDto;
import com.example.bugfixhub.dto.user.UserResDto;
import com.example.bugfixhub.entity.friend.Friend;
import com.example.bugfixhub.service.friend.FriendService;
import com.example.bugfixhub.session.Const;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    /**
     * 1. 친구 추가 기능
     */
    @PostMapping
    public ResponseEntity<FriendResDto> createFriendRequest(
            @RequestBody FriendReqDto friendReqDto,
            @SessionAttribute(name = Const.LOGIN_USER) UserResDto userResDto) {

        Friend friend = friendService.createFriendRequest(
                friendReqDto.getFollowingId(),
                userResDto.getId()
        );
        FriendResDto responseDto = new FriendResDto(
                friend.getId(),
                friend.getFollower().getId(),
                friend.getFollowing().getId(),
                friend.getStatus()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }


    /**
     * 2. 친구 요청 상태 변경 기능: Friend 고유 id로 조회
     */
    @PatchMapping("/{id}")
    public ResponseEntity<FriendResDto> updateFriendStatus(
            @PathVariable Long id,
            @RequestBody FriendReqStatusDto friendReqStatusDto) {

        Friend friend = friendService.updateFriendStatus(
                id, friendReqStatusDto.getStatus()
        );

        FriendResDto responseDto = new FriendResDto(
                friend.getId(),
                friend.getFollower().getId(),
                friend.getFollowing().getId(),
                friend.getStatus()

        );

        return ResponseEntity.ok(responseDto);

    }


    /**
     * 3. 친구 요청 전체 확인 기능:
     * status = accepted || rejected(거절) || unChecked(미응답) 조회
     */
    @GetMapping
    public ResponseEntity<List<FriendResDto>> getFriendRequests(
            @RequestParam(name = "status") String status,
            @SessionAttribute(name = Const.LOGIN_USER) UserResDto userResDto) {

        List<Friend> friendList = friendService.findAllFriendRequestsStatus(userResDto.getId(), status);

        List<FriendResDto> responseList = friendList.stream()
                .map(friend -> new FriendResDto(
                        friend.getId(),
                        friend.getFollower().getId(),
                        friend.getFollowing().getId(),
                        friend.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }


    /**
     * 4. 친구 삭제 기능
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriendRequest(@PathVariable Long id) {
        friendService.deleteFriendRequest(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}