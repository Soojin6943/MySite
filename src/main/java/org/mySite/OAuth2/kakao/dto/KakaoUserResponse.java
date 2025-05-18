package org.mySite.OAuth2.kakao.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoUserResponse {
    private Long id;
    private KakaoAccount kakao_account;

    @Getter
    public static class KakaoAccount {
        private Profile profile;

        @Getter
        public static class Profile {
            private String nickname;
        }
    }

}
