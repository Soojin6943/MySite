package org.mySite.user.service;

import lombok.RequiredArgsConstructor;
import org.mySite.user.domain.User;
import org.mySite.user.dto.JoinRequest;
import org.mySite.user.dto.LoginRequest;
import org.mySite.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getLoginUser(Long loginRequest){
        User user = userRepository.findById(loginRequest)
                .orElseThrow(() -> new IllegalArgumentException("UserId not found"));

        return user;
    }

    // ------ 회원가입 --------

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


    // ------로그인-------
    public User login(LoginRequest request){

        // 일치하는 User 찾기
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 찾은 User 비밀번호와 입력된 비밀번호 일치하는지 확인
        if(!user.getPassword().equals(request.getPassword())) {
            return null;
        }

        return user;
    }

    // Security 로그인에서 사용
    // 인증, 인가 시 사용
    // loginId가 null이거나 찾아온 User가 없으면 null 리턴
    public User getLoginUserByLoginId(String loginId){
        if (loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    // 회원가입 2
    // 스프링 시큐리티에서 사용
    // 비밀번호 암호화 사용
    public void join2(JoinRequest request){
        userRepository.save(request.toEntity(passwordEncoder.encode(request.getPassword())));
    }
}
