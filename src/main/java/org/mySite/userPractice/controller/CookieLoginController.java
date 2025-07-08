package org.mySite.userPractice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mySite.user.domain.User;
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
@Tag(name = "cookie로그인", description = "쿠키 로그인 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/cookie-login")
public class CookieLoginController {

    private final UserPracticeService userPracticeService;

    // 쿠키 로그인 페이지
    @GetMapping(value = {"", "/"})
    @Operation(summary = "쿠키 로그인 메인 페이지", description = "쿠키 로그인 동작을 위한 메인 페이지입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠키 로그인 페이지 로드 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
    })
    // @CookieValue 어노테이션을 통해 쿠키값 받아오기 가능
    public String home(@CookieValue(name = "userId", required = false) Long userId, Model model){
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        // 로그인 구현 전 임시 추가
        //model.addAttribute("nickname", "kk");

        if (userId != null) {
            UserPractice loginUser = userPracticeService.getLoginUser(userId);
            model.addAttribute("nickname", loginUser.getNickname());

        }

        return "home";
    }

    // 회원가입 페이지
    @GetMapping("/join")
    @Operation(summary = "[쿠키로그인] 회원가입 페이지", description = "회원가입 동작을 위한 메인 페이지입니다.")
    public String joinPage(Model model){
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    // 회원가입
    @PostMapping("/join")
    @Operation(summary = "[쿠키로그인] 회원가입 동작", description = "회원가입 요청 api 입니다.")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model){
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        // loginId 중복 체크
        if(userPracticeService.checkLoginId(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "중복된 아이디입니다."));
        }

        // 닉네임 중복 체크
        if (userPracticeService.checkNickname(joinRequest.getNickname())){
            bindingResult.addError(new FieldError("joinRequest", "nickname", "중복된 닉네임입니다."));
        }

        // password와 passwordCheck 확인
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "join";
        }

        userPracticeService.join(joinRequest);
        return "redirect:/cookie-login";
    }

    // 로그인 페이지
    @GetMapping("/login")
    @Operation(summary = "[쿠키로그인] 로그인 페이지", description = "로그인 동작을 위한 메인 페이지입니다.")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    // 로그인
    @PostMapping("/login")
    @Operation(summary = "[쿠키로그인] 로그인 페이지", description = "로그인 요청 api입니다.")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult, HttpServletResponse response, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        UserPractice user = userPracticeService.login(loginRequest);
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
    @Operation(summary = "[쿠키로그인] 로그아웃", description = "로그아웃 api 입니다.")
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
    @Operation(summary = "[쿠키로그인] 회원정보 페이지", description = "회원가입 페이지를 가져오는 api 입니다.")
    public String infoPage(@CookieValue(name = "userId", required = false) Long userId, Model model){
        model.addAttribute("pageName", "쿠키 로그인");
        model.addAttribute("loginType", "cookie-login");

        UserPractice user = userPracticeService.getLoginUser(userId);

        if (user == null){
            return "redirect:/cookie-login/login";
        }

        model.addAttribute("user", user);

        return "info";
    }

    // --------- 관리자 페이지 --------------
    @GetMapping("/admin")
    @Operation(summary = "[쿠키로그인] 관리자 페이지", description = "관리자 페이지입니다.")
    public String adminPage(@CookieValue(name = "userId", required = false) Long userId, Model model){
        model.addAttribute("pageName", "쿠키 로그인");
        model.addAttribute("loginType", "cookie-login");

        UserPractice user = userPracticeService.getLoginUser(userId);

        if (user == null){
            return "redirect:/cookie-login/login";
        }

        if (!user.getRole().equals(UserRole.ADMIN)){
            return "redirect:/cookie-login";
        }

        return "admin";
    }
}
