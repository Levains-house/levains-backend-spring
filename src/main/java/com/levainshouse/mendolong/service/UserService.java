package com.levainshouse.mendolong.service;

import com.levainshouse.mendolong.dto.user.UserSignInRequest;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.exception.UserNotFoundException;
import com.levainshouse.mendolong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private static final String TAG = "USER_SERVICE=%s";
    private final UserRepository userRepository;

    @Transactional
    public User signIn(UserSignInRequest signInRequest) {
        log.debug(String.format(TAG, "Sign in"));
        return userRepository.save(User.builder()
                .username(signInRequest.getUsername())
                .kakaoTalkChattingUrl(signInRequest.getKakaoTalkChattingUrl())
                .role(signInRequest.getRole())
                .build());
    }

    public User getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
    }

    public User validateNewUser(UserSignInRequest signInRequest){
        log.debug(String.format(TAG, "Validate new user"));
        return userRepository
                .findByUsername(signInRequest.getUsername())
                .orElse(null);
    }
}
