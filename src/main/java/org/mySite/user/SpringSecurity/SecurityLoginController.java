package org.mySite.user.SpringSecurity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mySite.user.domain.User;
import org.mySite.user.dto.JoinRequest;
import org.mySite.user.dto.LoginRequest;
import org.mySite.user.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/security-login")
public class SecurityLoginController {
    private final @Lazy UserService userService;

    // 로그인 홈 페이지
    @GetMapping(value={"", "/"})
    public String home(Model model, Authentication auth) {
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "Security 로그인");

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
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "Security 로그인");

        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    // 회원가입
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model){
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "Security 로그인");

        // loginId 중복 체크
        if(userService.checkLoginId(joinRequest.getLoginId())){
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디가 중복됨"));
        }

        // nickname 중복 체크
        if(userService.checkNickname(joinRequest.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다."));
        }

        // password랑 passwordCheck 같은지 확인
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())){
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        // 최종
        if(bindingResult.hasErrors()){
            return "join";
        }

        userService.join2(joinRequest);
        return "redirect:/security-login";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "Security 로그인");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    // info
    @GetMapping("/info")
    public String info(Model model, Authentication auth){
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "Security 로그인");

        User loginUser = userService.getLoginUserByLoginId(auth.getName());

        if(loginUser == null) {
            return "redirect:/security-login/login";
        }

        model.addAttribute("user", loginUser);
        return "info";
    }

    // admin
    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "Security 로그인");

        return "admin";
    }

}
