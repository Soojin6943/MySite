package org.mySite.user.SpringSecurity;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.mySite.user.domain.UserRole;
import org.mySite.user.jwt.JwtTokenFilter;
import org.mySite.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
     //spring Security 로그인에서 사용 + JWT
     @Bean
     public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
     }


     //spring security 사용
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

    //---------------------------

    // JWT 로그인에서 사용
    private final @Lazy JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
         httpSecurity
                .httpBasic(Customizer.withDefaults()) // Http Basic 인증 비활성화
                .csrf(csrf -> csrf.disable())   // csrf 보호 비활성화(jwt는 상태가 없기 때문)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함 + 필수 설정
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)    // JWT 필터 추가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/jwt-login/info").authenticated() // 인증 필요
                        .requestMatchers("jwt-login/admin/**").hasAuthority(UserRole.ADMIN.name()) // ADMIN 권한 필요
                        .anyRequest().permitAll() // 그 외 요청은 모두 허용
                );
         return httpSecurity.build();
    }
}
