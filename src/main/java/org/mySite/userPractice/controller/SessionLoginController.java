package org.mySite.userPractice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mySite.user.domain.UserRole;
import org.mySite.user.dto.LoginRequest;
import org.mySite.userPractice.domain.UserPractice;
import org.mySite.userPractice.dto.JoinRequest;
import org.mySite.userPractice.service.UserPracticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/session-login")
public class SessionLoginController {

    private final UserPracticeService userPracticeService;

    // 로그인 홈 페이지
    @GetMapping(value = {"", "/"})
    public String home(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        if (userId != null){
            UserPractice loginUser = userPracticeService.getLoginUser(userId);
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
        if(userPracticeService.checkLoginId(joinRequest.getLoginId())){
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디가 중복됩니다."));
        }

        // nickname 중복 체크
        if(userPracticeService.checkNickname(joinRequest.getNickname())){
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다"));
        }

        // 비밀번호 + 비밀번호 체크 동일 확인
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다"));
        }

        // 최종 확인
        if(bindingResult.hasErrors()){
            return "join";
        }

        userPracticeService.join(joinRequest);
        return "redirect:/session-login";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult, HttpServletRequest httpServletRequest, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        UserPractice user = userPracticeService.login(loginRequest);

        if(user == null) {
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }

        // 로그인 실패 시 로그인 페이지로 반환
        if(bindingResult.hasErrors()){
            return "login";
        }

        // 로그인 성공 -> 세션 생성
        // 세션 생성 전 기존 세션 파기
        httpServletRequest.getSession().invalidate();
        // Session이 없으면 생성
        HttpSession session = httpServletRequest.getSession(true);
        // 세션에 userId를 넣어줌
        session.setAttribute("userId", user.getId());
        // session 유지 시간 30분
        session.setMaxInactiveInterval(1800);

        return "redirect:/session-login";
    }

    // logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        // false -> session이 없으면 null return
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/session-login";
    }

    // info
    @GetMapping("/info")
    public String info(@SessionAttribute(name = "userId", required = false) Long userId, Model model){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        UserPractice loginUser = userPracticeService.getLoginUser(userId);

        if(loginUser == null){
            return "redirect:/session-login/login";
        }

        model.addAttribute("user", loginUser);
        return "info";
    }

    // admin
    @GetMapping("/admin")
    public String admin(@SessionAttribute(name = "userId", required = false) Long userId, Model model){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        UserPractice loginUser = userPracticeService.getLoginUser(userId);

        if(loginUser == null){
            return "redirect:/session-login/login";
        }

        if(!loginUser.getRole().equals(UserRole.ADMIN)) {
            return "redirect:/session-login";
        }

        return "admin";
    }
}
