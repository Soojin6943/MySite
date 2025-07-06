package org.mySite.user.service;

import lombok.RequiredArgsConstructor;
import org.mySite.user.SpringSecurity.PrincipalDetails;
import org.mySite.user.domain.User;
import org.mySite.user.dto.JoinRequest;
import org.mySite.user.dto.LoginRequest;
import org.mySite.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getLoginUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserId not found"));

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

    // spring security + jwt 사용
    // 비밀번호 암호화 저장
    public void join2(JoinRequest request){
        userRepository.save(request.toEntity(passwordEncoder.encode(request.getPassword())));
    }

    // ------로그인-------
//    @Override
//    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
//        User user = userRepository.findByLoginId(loginId)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new PrincipalDetails(user);
//    }
}
