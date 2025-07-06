package org.mySite.user.controller;

import lombok.RequiredArgsConstructor;
import org.mySite.user.domain.User;
import org.mySite.user.dto.JoinRequest;
import org.mySite.user.dto.LoginRequest;
import org.mySite.user.jwt.JwtTokenUtil;
import org.mySite.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @GetMapping(value={"", "/"})
    public String home(Model model, Authentication auth) {
        model.addAttribute("loginType", "auth");
        model.addAttribute("pageName", "로그인");

        if (auth != null){
            User loginUser = userService.getLoginUserByLoginId(auth.getName());
            if (loginUser != null){
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }
        return "home";
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model){
        model.addAttribute("loginType", "auth");
        model.addAttribute("pageName", "회원가입");

        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String register(@ModelAttribute JoinRequest joinRequest) {
        if (userService.checkLoginId(joinRequest.getLoginId())) return "로그인 아이디가 중복됩니다.";
        if (userService.checkNickname(joinRequest.getNickname())) return "닉네임이 중복됩니다.";
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) return "비밀번호가 일치하지 않습니다.";

        userService.join2(joinRequest);
        return "home";
    }

    // -------로그인 -----------

    // 회원가입 페이지
    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("loginType", "auth");
        model.addAttribute("pageName", "로그인");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {

        User user = userService.getLoginUserByLoginId(loginRequest.getLoginId());

        // 로그인 실패 시 에러 반환
        // TODO 비밀번호 확인 수정
        if(user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){

            return "로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }

        // 로그인 성공 시 Jwt Token 발급
        long expireTimeMs = 1000 * 60 * 60;     // 60분

        String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);

        return jwtToken;
    }

    @GetMapping("/info")
    @ResponseBody
    public String userInfo(Authentication auth) {
        User loginUser = userService.getLoginUserByLoginId(auth.getName());

        return String.format("loginId: %s\n nickname: %s\n role: %s", loginUser.getLoginId(), loginUser.getNickname(), loginUser.getRole().name());

    }

    @GetMapping("/admin")
    public String adminPage() {
        return "관리자 페이지 접근 성공";
    }
}

