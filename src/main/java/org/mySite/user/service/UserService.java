package org.mySite.user.service;

import lombok.RequiredArgsConstructor;
import org.mySite.user.dto.JoinRequest;
import org.mySite.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // loginId 중복 체크
    public boolean checkLoginId(String loginId) {
        // 1건이라도 있으면 바로 스캔 종료 후 true 리턴
        return userRepository.existsByLoginId(loginId);
    }

    // nickname 중복 체크
    public boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // JoinRequest을 입력 받아 User로 변환
    public void join(JoinRequest request) {
        userRepository.save(request.toEntity());
    }
}
