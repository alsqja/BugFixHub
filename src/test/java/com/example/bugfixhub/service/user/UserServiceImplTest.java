package com.example.bugfixhub.service.user;

import com.example.bugfixhub.config.PasswordEncoder;
import com.example.bugfixhub.dto.user.CreateUserReqDto;
import com.example.bugfixhub.dto.user.UpdateUserReqDto;
import com.example.bugfixhub.dto.user.UserResDto;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void signUp() {
        // given
        String testName = "testName";
        String testEmail = "email@email.com";
        String testPassword = "0000";
        String encodedPassword = "encodedPassword";
        User user = new User(testName, testEmail, encodedPassword);
        CreateUserReqDto dto = new CreateUserReqDto(testName, testEmail, testPassword);
        when(userRepository.findByEmail(eq(testEmail))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(eq(testPassword))).thenReturn(encodedPassword);

        // when
        UserResDto savedUser = userService.signUp(dto);

        // then
        assertEquals(testName, savedUser.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateSuccess() {
        // given
        Long testIdWithPassword = 1L;
        Long testId = 2L;
        String testName = "testName";
        String testEmail = "testEmail";
        String testUpdateName = "testUpdateName";
        String testOldPassword = "testOldPassword";
        String testNewPassword = "testNewPassword";
        User userWithPassword = new User(testIdWithPassword, testName, testEmail, testOldPassword);
        User user = new User(testId, testName, testEmail, testOldPassword);
        when(userRepository.findByIdOrElseThrow(eq(testIdWithPassword))).thenReturn(userWithPassword);
        when(userRepository.findByIdOrElseThrow(eq(testId))).thenReturn(user);
        when(passwordEncoder.matches(eq(testOldPassword), eq(testOldPassword))).thenReturn(true);
        UpdateUserReqDto dtoWithPassword = new UpdateUserReqDto(testUpdateName, testOldPassword, testNewPassword);
        UpdateUserReqDto dto = new UpdateUserReqDto(testUpdateName, null, null);

        // when
        UserResDto updatedUserWithPassword = userService.update(testIdWithPassword, dtoWithPassword);
        UserResDto updatedUser = userService.update(testId, dto);

        // then
        verify(userRepository).findByIdOrElseThrow(eq(testIdWithPassword));
        assertEquals(testIdWithPassword, updatedUserWithPassword.getId());
        assertEquals(testUpdateName, updatedUserWithPassword.getName());
        verify(userRepository).findByIdOrElseThrow(eq(testId));
        assertEquals(testId, updatedUser.getId());
        assertEquals(testUpdateName, updatedUser.getName());
    }

    @Test
    void updateException() {
        // given
        Long testId = 1L;
        Long testDeletedId = 2L;
        String testName = "testName";
        String testEmail = "testEmail";
        String testUpdateName = "testUpdateName";
        String testOldPassword = "testOldPassword";
        String testWrongOldPassword = "wrongPassword";
        String testNewPassword = "testNewPassword";
        User testUser = new User(testId, testName, testEmail, testOldPassword);
        User testDeletedUser = new User(testDeletedId, testName, testEmail, testOldPassword, true);
        when(userRepository.findByIdOrElseThrow(eq(testId))).thenReturn(testUser);
        when(userRepository.findByIdOrElseThrow(eq(testDeletedId))).thenReturn(testDeletedUser);
        when(passwordEncoder.matches(eq(testOldPassword), eq(testOldPassword))).thenReturn(true);
        when(passwordEncoder.matches(eq(testWrongOldPassword), eq(testOldPassword))).thenReturn(false);

        UpdateUserReqDto dto = new UpdateUserReqDto(testUpdateName, testOldPassword, testNewPassword);
        UpdateUserReqDto nullNewPasswordDto = new UpdateUserReqDto(testUpdateName, testOldPassword, null);
        UpdateUserReqDto nullOldPasswordDto = new UpdateUserReqDto(testUpdateName, null, testNewPassword);
        UpdateUserReqDto wrongOldPasswordDto = new UpdateUserReqDto(testUpdateName, testWrongOldPassword, testNewPassword);
        UpdateUserReqDto samePasswordDto = new UpdateUserReqDto(testUpdateName, testOldPassword, testOldPassword);

        // when
        ResponseStatusException deletedErr = assertThrows(ResponseStatusException.class, () -> userService.update(testDeletedId, dto));
        ResponseStatusException nullNewPasswordErr = assertThrows(ResponseStatusException.class, () -> userService.update(testId, nullNewPasswordDto));
        ResponseStatusException nullOldPasswordErr = assertThrows(ResponseStatusException.class, () -> userService.update(testId, nullOldPasswordDto));
        ResponseStatusException wrongOldPasswordErr = assertThrows(ResponseStatusException.class, () -> userService.update(testId, wrongOldPasswordDto));
        ResponseStatusException samePasswordErr = assertThrows(ResponseStatusException.class, () -> userService.update(testId, samePasswordDto));

        // then
        assertEquals("탈퇴된 회원입니다", deletedErr.getReason());
        assertEquals("새 비밀번호를 입력해주세요.", nullNewPasswordErr.getReason());
        assertEquals("기존 비밀번호를 입력해주세요.", nullOldPasswordErr.getReason());
        assertEquals("잘못된 비밀번호입니다.", wrongOldPasswordErr.getReason());
        assertEquals("동일한 비밀번호로 변경이 불가능합니다.", samePasswordErr.getReason());
    }
}