package org.mySite.SpringSecurity;

import org.mySite.user.domain.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 인증, 인가 필요한 url 지정
                        .requestMatchers("/security-login/info").authenticated()    //authenticated 해당 url에 진입하기 위해 authentication(인증, 로그인)이 필요함
                        .requestMatchers("/security-login/admin/**").hasAuthority(UserRole.ADMIN.name())    // 해당 url 집입하기 위해서 Authorization(인가, ex)권한이 ADMIN인 유저만 집입가능)이 필요함
                        .anyRequest().permitAll()   // 그외의 모든 url / authentication, authorization 필요 없이 통과
                )
                .formLogin(form -> form
                        // form login 방식 적용
                        .usernameParameter("loginId")   // 로그인할 때 사용되는 id를 적어줌(여기서는 loginId로 로그인 하기 때문에 따로 적어줌. userName으로 로그인 한다면 적어주지 않아도 됨)
                        .passwordParameter("password")  // 로그인할 때 사용되는 password를 적어줌
                        .loginPage("/security-login/login") // 로그인 페이지 url
                        .defaultSuccessUrl("/security-login", true)   // 로그인 성공 시 이동할 url
                        .failureUrl("/security-login/login")    // 로그인 실패 시 이동할 url

                )
                .logout(logout -> logout
                        // 로그아웃에 대한 정보
                        .logoutUrl("/security-login/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }
}
