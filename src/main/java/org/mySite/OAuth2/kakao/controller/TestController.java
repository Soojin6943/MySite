package org.mySite.OAuth2.kakao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class TestController {

    private final KaKaoApi kaKaoApi;

    @GetMapping("kako/login")
    public String loginForm(Model model){
        model.addAttribute("kakaoApiKey", kaKaoApi.getKakaoApiKey());
        model.addAttribute("redirectUri", kaKaoApi.getKakaoRedirectUri());
        return "Oauth2";
    }

    // 토큰 받기 + 사용자 정보 받기
    @RequestMapping("/oauth/kakao")
    public String kakaoLogin(@RequestParam String code){
        // 토큰 받기
        String tokenResponse = kaKaoApi.getAccessToken(code);
        System.out.println("카카오 토큰 응답 : " + tokenResponse);

        return "redirect:/";
    }
}
