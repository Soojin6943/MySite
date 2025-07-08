package org.mySite.userPractice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "로그인 시도한 아이디", example = "soojin")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Schema(description = "입력된 비밀번호", example = "1234")
    private String password;

    @NotBlank
    @Schema(description = "입력된 비밀번호 확인", example = "1234")
    private String passwordCheck;

    @NotBlank(message = "닉네임을 입력하세요")
    @Schema(description = "입력된 닉네임", example = "soojin")
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
