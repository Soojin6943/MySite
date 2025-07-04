package org.mySite.userPractice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mySite.user.domain.UserRole;
import org.mySite.userPractice.domain.UserPractice;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
    @NotBlank(message = "로그인 아이디를 입력하세요")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    @NotBlank
    private String passwordCheck;

    @NotBlank(message = "닉네임을 입력하세요")
    private String nickname;

    public UserPractice toEntity() {
        return UserPractice.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .role(UserRole.USER)
                .build();
    }
}
