package org.mySite.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mySite.user.domain.User;
import org.mySite.user.domain.UserRole;
import org.mySite.user.dto.JoinRequest;
import org.mySite.user.dto.LoginRequest;
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
    // @CookieValue 어노테이션을 통해 쿠키값 받아오기 가능
    public String home(@CookieValue(name = "userId", required = false) Long userId, Model model){
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        // 로그인 구현 전 임시 추가
        //model.addAttribute("nickname", "kk");

        if (userId != null) {
            User loginUser = userService.getLoginUser(userId);
            model.addAttribute("nickname", loginUser.getNickname());

        }

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

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult, HttpServletResponse response, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        User user = userService.login(loginRequest);
        // System.out.println("입력된 로그인 ID: " + loginRequest.getLoginId());


        // 틀릴 경우 global error return
        if(user == null){
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }

        if(bindingResult.hasErrors()) {
            return "login";
        }

        // 로그인 성공 => 쿠키 생성
        Cookie cookie = new Cookie("userId", String.valueOf(user.getId()));
        cookie.setMaxAge(60*60); // 유효 시간 1시간
        response.addCookie(cookie);

        return "redirect:/cookie-login";
    }

    // -----------로그아웃 ------------------
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, Model model){
        model.addAttribute("pageName", "쿠키 로그인");
        model.addAttribute("loginType", "cookie-login");

        Cookie cookie = new Cookie("userId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/cookie-login";
    }


    // ------------ 회원 정보 ------------------
    @GetMapping("/info")
    public String infoPage(@CookieValue(name = "userId", required = false) Long userId, Model model){
        model.addAttribute("pageName", "쿠키 로그인");
        model.addAttribute("loginType", "cookie-login");

        User user = userService.getLoginUser(userId);

        if (user == null){
            return "redirect:/cookie-login/login";
        }

        model.addAttribute("user", user);

        return "info";
    }

    // --------- 관리자 페이지 --------------
    @GetMapping("/admin")
    public String adminPage(@CookieValue(name = "userId", required = false) Long userId, Model model){
        model.addAttribute("pageName", "쿠키 로그인");
        model.addAttribute("loginType", "cookie-login");

        User user = userService.getLoginUser(userId);

        if (user == null){
            return "redirect:/cookie-login/login";
        }

        if (!user.getRole().equals(UserRole.ADMIN)){
            return "redirect:/cookie-login";
        }

        return "admin";
    }
}
