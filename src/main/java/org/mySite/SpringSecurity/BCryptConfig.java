package org.mySite.SpringSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BCryptConfig {
    // Spring Security에서 비밀번호를 암호화 해주는 BCryptPasswordEncoder 존재
    // Spring에 등록해놓고 비밀번호 암호화, 비밀번호 체크할 때 사용하면 됨
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
