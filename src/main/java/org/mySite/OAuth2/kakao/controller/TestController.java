package org.mySite.OAuth2.kakao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/kakao")
public class TestController {

    // client_id에 들어감
    @Value("${kakao.api_key}")
    private String kakaoApiKey;

    // redirect_uri에 들어감
    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        model.addAttribute("redirectUri", kakaoRedirectUri);
        return "Oauth2";
    }
}
