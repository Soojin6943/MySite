package org.mySite.user.jwt;

import lombok.RequiredArgsConstructor;
import org.mySite.user.domain.User;
import org.mySite.user.dto.JoinRequest;
import org.mySite.user.dto.LoginRequest;
import org.mySite.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JwtLoginApiController {

    private final UserService userService;

    @PostMapping("/join")
    public String join(@RequestBody JoinRequest joinRequest) {

        // loginId 중복 체크
        if(userService.checkLoginId(joinRequest.getLoginId())) {
            return "로그인 아이디가 중복됩니다.";
        }

        // 닉네임 중복 체크
        if(userService.checkNickname(joinRequest.getNickname())) {
            return "닉네임이 중복됩니다.";
        }

        // password + passwordCheck 확임
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return "비밀번호가 일치하지 않습니다.";
        }

        //userService.join2(joinRequest);
        userService.join(joinRequest);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {

        User user = userService.login(loginRequest);

        // 로그인 실패 시 에러 반환
        if(user == null){
            return "로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }

        // 로그인 성공 시 Jwt Token 발급
        String secretKey = "dGhpcy1pcy1hLXZlcnktc2VjdXJlLXNlY3JldC1rZXktMTIzNDU2Nzg5MCE=";
        long expireTimeMs = 1000 * 60 * 60;     // 60분

        String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);

        return jwtToken;
    }

    @GetMapping("/info")
    public String userInfo(Authentication auth) {
        User loginUser = userService.getLoginUserByLoginId(auth.getName());

        return String.format("loginId: %s\n nickname: %s\n role: %s", loginUser.getLoginId(), loginUser.getNickname(), loginUser.getRole().name());

    }

    @GetMapping("/admin")
    public String adminPage() {
        return "관리자 페이지 접근 성공";
    }
}
