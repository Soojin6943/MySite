package org.mySite.OAuth2.kakao.controller;

import lombok.Getter;
import org.mySite.OAuth2.kakao.dto.KakaoTokenResponse;
import org.mySite.OAuth2.kakao.dto.KakaoUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@Getter
public class KaKaoApi {
    // client_id에 들어감
    @Value("${kakao.api_key}")
    private String kakaoApiKey;

    // redirect_uri에 들어감
    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.logout_redirect_uri}")
    private String kakaoLogoutUri;

    // 인증 코드를 사용하여 토큰 요청
    public KakaoTokenResponse getAccessToken(String code){
        RestTemplate restTemplate = new RestTemplate(); // 동기식 (요청보내면 응답 받을 때까지 블로킹)

        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        // 필수 body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoApiKey);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        // 필수 header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        // 카카오로부터 받은 응답
        ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(tokenUrl, request, KakaoTokenResponse.class);

        return response.getBody();
    }

    // 사용자 정보 요청
    public KakaoUserResponse getUserInfo(String accessToken){
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                KakaoUserResponse.class
        );

        return response.getBody();
    }

}
