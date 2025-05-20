package org.mySite.OAuth2.kakao.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.mySite.OAuth2.kakao.Entity.KakaoUser;
import org.mySite.OAuth2.kakao.dto.KakaoTokenResponse;
import org.mySite.OAuth2.kakao.dto.KakaoUserResponse;
import org.mySite.OAuth2.kakao.repository.KakaoUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class TestController {

    private final KaKaoApi kaKaoApi;
    private final KakaoUserRepository kakaoUserRepository;

    // 1. 인가코드 받기
    @GetMapping("kakao/login")
    public String loginForm(Model model){
        model.addAttribute("kakaoApiKey", kaKaoApi.getKakaoApiKey());
        model.addAttribute("redirectUri", kaKaoApi.getKakaoRedirectUri());
        return "Oauth2";
    }

    // 2. 토큰 받기 + 3. 사용자 정보 받기
    @RequestMapping("/oauth/kakao")
    public String kakaoLogin(@RequestParam String code, HttpSession session){
        // 2. 토큰 받기
        KakaoTokenResponse tokenResponse = kaKaoApi.getAccessToken(code);
        System.out.println("카카오 토큰 응답 : " + tokenResponse);
        // access token만 빼오기
        String accessToken = tokenResponse.getAccessToken();

        // 3. 사용자 정보 요청
        KakaoUserResponse userInfo = kaKaoApi.getUserInfo(accessToken);

        String kakaoId = String.valueOf(userInfo.getId());  // kakao에서 넘겨주는 id
        String nickname = userInfo.getKakao_account().getProfile().getNickname();

        // 4. 우리 DB 사용자 존재 여부 확인
        Optional<KakaoUser> optionalKakaoUser = kakaoUserRepository.findByKakaoId(kakaoId);
        KakaoUser loginUser;

        if (optionalKakaoUser.isPresent()){
            loginUser = optionalKakaoUser.get();
        } else {
            KakaoUser newUser = new KakaoUser();
            newUser.setKakaoId(kakaoId);
            newUser.setNickname(nickname);
            loginUser = kakaoUserRepository.save(newUser);
        }

        // 5. 세션에 로그인 정보 저장
        session.setAttribute("loginUser", loginUser);
        System.out.println("로그인 완료 "+ loginUser);
        return "redirect:/kakao";
    }

    // 0. 로그인시 후 메인 화면
    @GetMapping("/kakao")
    public String kakaoLoginMain(Model model, @SessionAttribute(value = "loginUser", required = false) KakaoUser loginUser){
        model.addAttribute("loginType", "kakao_login");
        model.addAttribute("pageName", "카카오 로그인");

        if (loginUser != null){
            String nickname = loginUser.getNickname();
            model.addAttribute("nickname", nickname);
        }

        return "home";
    }
}
