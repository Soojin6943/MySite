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
@RequestMapping("/cookie-login")
public class CookieLoginController {

    private final UserService userService;

    // 쿠키 로그인 페이지
    @GetMapping(value = {"", "/"})
    public String home(@CookieValue(name = "userId", required = false) Long userId, Model model){
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        // 로그인 구현 전 임시 추가
        //model.addAttribute("nickname", "kk");

        // User loginUser = userService.getLoginUser(userId);

//        if(loginUser != null) {
//            model.addAttribute("nickname", loginUser.getNickname());
//        }
        return "home";
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model){
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    // 회원가입
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model){
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        // loginId 중복 체크
        if(userService.checkLoginId(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "중복된 아이디입니다."));
        }

        // 닉네임 중복 체크
        if (userService.checkNickname(joinRequest.getNickname())){
            bindingResult.addError(new FieldError("joinRequest", "nickname", "중복된 닉네임입니다."));
        }

        // password와 passwordCheck 확인
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "join";
        }

        userService.join(joinRequest);
        return "redirect:/cookie-login";
    }
}
