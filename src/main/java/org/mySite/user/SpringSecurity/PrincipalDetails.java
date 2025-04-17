package org.mySite.user.SpringSecurity;

import org.mySite.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {
    // 직접 로그인 처리를 안해도 되는 대신
    // 지정해줘야하는 정보들
    // POST /login에 대한 요청을 security가 가로채서 진행해주기 때문에 진접 매핑해서 만들 필요 없음
    // 로그인 성공 시 Security Session을 생성해 줌(Key값: Security ContextHolder)
    // Security Session(Authentication(UserDetails)) 이런식의 구조로 되어있는데 PrincipalDetails에서 UserDetails를 설정해준다고 보면 됨

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 권한 관련 작업을 하기 위한 role return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collections = new ArrayList<>();
        collections.add(() -> {
            return user.getRole().name();
        });

        return collections;
    }

    // get Password 메서드
    @Override
    public String getPassword(){
        return user.getPassword();
    }

    // get Username 메서드 (생성한 User는 loginId 사용)
    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    // 계정 만료 확인 (true: 만료 X)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠겼는지 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되었는지 (true: 만료 X)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화(사용가능)인지 (true: 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }


}
