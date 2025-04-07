package org.mySite.user.controller;

import lombok.RequiredArgsConstructor;
import org.mySite.user.domain.User;
import org.mySite.user.dto.JoinRequest;
import org.mySite.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
@RequestMapping("/session-login")
public class SessionLoginController {

    private final UserService userService;

    // 로그인 홈 페이지
    @GetMapping(value = {"", "/"})
    public String home(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        if (userId != null){
            User loginUser = userService.getLoginUser(userId);
            model.addAttribute("nickname", loginUser.getNickname());
        }

        return "home";
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        // TODO JoinRequest 생성 이유 알아보기
        // JoinRequest를 작성하는 이유 알아보기
        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }
}
