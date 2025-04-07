package org.mySite.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mySite.user.domain.User;
import org.mySite.user.dto.JoinRequest;
import org.mySite.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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

    // 회원가입
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        // id 중복 체크
        if(userService.checkLoginId(joinRequest.getLoginId())){
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디가 중복됩니다."));
        }

        // nickname 중복 체크
        if(userService.checkNickname(joinRequest.getNickname())){
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다"));
        }

        // 비밀번호 + 비밀번호 체크 동일 확인
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다"));
        }

        // 최종
        if(bindingResult.hasErrors()){
            return "join";
        }

        userService.join(joinRequest);
        return "redirect:/session-login";
    }
}
