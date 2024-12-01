package com.example.bugfixhub.controller.friend;

import com.example.bugfixhub.dto.friend.FriendReqDto;
import com.example.bugfixhub.dto.friend.FriendReqStatusDto;
import com.example.bugfixhub.dto.friend.FriendResDto;
import com.example.bugfixhub.dto.friend.FriendUserResDto;
import com.example.bugfixhub.dto.user.UserResDto;
import com.example.bugfixhub.service.friend.FriendService;
import com.example.bugfixhub.session.Const;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
            @SessionAttribute(name = Const.LOGIN_USER) UserResDto userResDto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(friendService.createFriendRequest(friendReqDto.getFollowingId(), userResDto.getId()));
    }


    /**
     * 2. 친구 요청 상태 변경 기능: Friend 고유 id로 조회
     */
    @PatchMapping("/{id}")
    public ResponseEntity<FriendResDto> updateFriendStatus(
            @PathVariable Long id,
            @RequestBody FriendReqStatusDto friendReqStatusDto,
            @SessionAttribute UserResDto loginUser
    ) {

        return ResponseEntity.ok(friendService.updateFriendStatus(id, friendReqStatusDto.getStatus().getValue(), loginUser.getId()));
    }


    /**
     * 3. 친구 요청 전체 확인 기능:
     * status = accepted || rejected(거절) || unChecked(미응답) 조회
     */
    @GetMapping
    public ResponseEntity<List<FriendUserResDto>> getFriendRequests(
            @RequestParam(name = "status") String status,
            @SessionAttribute(name = Const.LOGIN_USER) UserResDto userResDto
    ) {
        if (status.equals("accepted")) {
            return ResponseEntity.ok(friendService.findAllFriends(userResDto.getId()));
        } else if (status.equals("unChecked")) {
            return ResponseEntity.ok(friendService.findAllFriendReqs(userResDto.getId()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status 를 확인해 주세요.");
        }
    }


    /**
     * 4. 친구 삭제 기능
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriendRequest(
            @PathVariable Long id,
            @SessionAttribute UserResDto loginUser
    ) {
        friendService.deleteFriendRequest(id, loginUser.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}