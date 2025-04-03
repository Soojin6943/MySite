package org.mySite.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO @setter 없이 해보기
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    private String loginId;
    private String password;
}
