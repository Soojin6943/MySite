package org.mySite.userPractice.service;

import lombok.RequiredArgsConstructor;
import org.mySite.user.dto.LoginRequest;
import org.mySite.userPractice.domain.UserPractice;
import org.mySite.userPractice.dto.JoinRequest;
import org.mySite.userPractice.repository.UserPracticeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPracticeService {
    private final UserPracticeRepository userPracticeRepository;

    public UserPractice getLoginUser(Long loginRequest){
        UserPractice user = userPracticeRepository.findById(loginRequest)
                .orElseThrow(() -> new IllegalArgumentException("UserId not found"));

        return user;
    }

    // 회원가입
    // 아이디 중복 체크
    public boolean checkLoginId(String loginId) {
        // 1건이라도 있으면 바로 스캔 종료 후 true 리턴
        return userPracticeRepository.existsByLoginId(loginId);
    }

    // nickname 중복 체크
    public boolean checkNickname(String nickname) {
        return userPracticeRepository.existsByNickname(nickname);
    }

    public void join(JoinRequest request) {
        userPracticeRepository.save(request.toEntity());
    }

    // login
    public UserPractice login(LoginRequest request){

        // 일치하는 user 찾기
        UserPractice user = userPracticeRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 비밀번호 확인
        if (!user.getPassword().equals(request.getPassword())) {
            return null;
        }

        return user;
    }
}
