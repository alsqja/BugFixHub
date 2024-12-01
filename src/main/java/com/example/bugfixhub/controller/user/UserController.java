package com.example.bugfixhub.controller.user;

import com.example.bugfixhub.dto.post.GetAllUserPostResDto;
import com.example.bugfixhub.dto.user.CheckPasswordReqDto;
import com.example.bugfixhub.dto.user.CreateUserReqDto;
import com.example.bugfixhub.dto.user.LoginReqDto;
import com.example.bugfixhub.dto.user.UpdateUserReqDto;
import com.example.bugfixhub.dto.user.UserDetailResDto;
import com.example.bugfixhub.dto.user.UserResDto;
import com.example.bugfixhub.service.user.UserService;
import com.example.bugfixhub.session.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResDto> signUp(@Valid @RequestBody CreateUserReqDto dto) {
        return new ResponseEntity<>(userService.signUp(dto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResDto> login(
            @Valid @RequestBody LoginReqDto dto,
            HttpServletRequest request
    ) {
        UserResDto findUser = userService.login(dto);

        HttpSession session = request.getSession();

        session.setAttribute(Const.LOGIN_USER, findUser);

        return new ResponseEntity<>(findUser, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResDto> findById(
            @PathVariable Long id,
            @SessionAttribute UserResDto loginUser
    ) {

        return new ResponseEntity<>(userService.findById(id, loginUser.getId()), HttpStatus.OK);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<GetAllUserPostResDto> findAllUserPost(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @SessionAttribute UserResDto loginUser
    ) {

        return new ResponseEntity<>(userService.findAllUserPost(id, loginUser.getId(), page - 1, limit), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<UserResDto> update(
            @Valid @RequestBody UpdateUserReqDto dto,
            @SessionAttribute UserResDto loginUser
    ) {

        return new ResponseEntity<>(userService.update(loginUser.getId(), dto), HttpStatus.OK);
    }

    @PostMapping("/password")
    public ResponseEntity<Void> checkPassword(
            @Valid @RequestBody CheckPasswordReqDto dto,
            @SessionAttribute UserResDto loginUser
    ) {
        userService.checkPassword(dto.getPassword(), loginUser.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @SessionAttribute UserResDto loginUser,
            HttpServletRequest request
    ) {
        userService.delete(loginUser.getId());

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
