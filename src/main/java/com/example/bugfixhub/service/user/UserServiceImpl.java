package com.example.bugfixhub.service.user;

import com.example.bugfixhub.config.PasswordEncoder;
import com.example.bugfixhub.dto.post.GetAllPostResDataDto;
import com.example.bugfixhub.dto.post.GetAllUserPostResDto;
import com.example.bugfixhub.dto.user.CreateUserReqDto;
import com.example.bugfixhub.dto.user.LoginReqDto;
import com.example.bugfixhub.dto.user.UpdateUserReqDto;
import com.example.bugfixhub.dto.user.UserDetailResDto;
import com.example.bugfixhub.dto.user.UserResDto;
import com.example.bugfixhub.entity.post.Post;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.repository.post.PostRepository;
import com.example.bugfixhub.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResDto signUp(CreateUserReqDto dto) {

        Optional<User> findUser = userRepository.findByEmail(dto.getEmail());

        if (findUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }

        String password = passwordEncoder.encode(dto.getPassword());

        User saveUser = new User(dto.getName(), dto.getEmail(), password);

        return new UserResDto(userRepository.save(saveUser));
    }

    @Override
    public UserResDto login(LoginReqDto dto) {
        User findUser = userRepository.findByEmailOrElseThrow(dto.getEmail());

        isDeleted(findUser);

        if (!passwordEncoder.matches(dto.getPassword(), findUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호를 확인해주세요.");
        }

        return new UserResDto(findUser);
    }

    @Override
    public UserDetailResDto findById(Long id, Long myId) {
        User findUser = userRepository.findByIdOrElseThrow(id);

        isDeleted(findUser);

        return new UserDetailResDto(findUser, myId);
    }

    @Transactional
    @Override
    public UserResDto update(Long id, UpdateUserReqDto dto) {
        User findUser = userRepository.findByIdOrElseThrow(id);

        isDeleted(findUser);

        if (dto.getOldPassword() == null && dto.getNewPassword() == null) {
            if (dto.getName() != null) {
                findUser.setName(dto.getName());
            }
        } else {
            if (dto.getNewPassword() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "새 비밀번호를 입력해주세요.");
            }
            if (dto.getOldPassword() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "기존 비밀번호를 입력해주세요.");
            }
            if (!passwordEncoder.matches(dto.getOldPassword(), findUser.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.");
            }
            if (dto.getNewPassword().equals(dto.getOldPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "동일한 비밀번호로 변경이 불가능합니다.");
            }
            if (dto.getName() != null) {
                findUser.setName(dto.getName());
            }

            findUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        }
        return new UserResDto(findUser);
    }

    @Override
    public void checkPassword(String password, Long id) {
        User findUser = userRepository.findByIdOrElseThrow(id);

        isDeleted(findUser);

        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.");
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        User findUser = userRepository.findByIdOrElseThrow(id);

        isDeleted(findUser);

        userRepository.delete(findUser);
    }

    @Override
    public GetAllUserPostResDto findAllUserPost(Long id, Long loginUserId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        Page<Post> postsPage = postRepository.findByUserIdAndDeletedFalse(id, pageable);

        Page<GetAllPostResDataDto> posts = postsPage.map(post -> new GetAllPostResDataDto(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getName(),
                post.getTitle(),
                post.getType().getValue(),
                post.getComments().size(),
                post.getLikes().size(),
                post.getLikes().stream().anyMatch(i -> i.getUser().getId().equals(loginUserId)),
                post.getCreatedAt(),
                post.getUpdatedAt()
        ));

        return new GetAllUserPostResDto((long) posts.getTotalPages(), posts.getTotalElements(), posts.getContent());
    }

    private void isDeleted(User user) {
        if (user.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "탈퇴된 회원입니다");
        }
    }
}
