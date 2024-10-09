package com.kemslogin.security.oauth2;

import com.kemslogin.user.model.domain.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    //일반 로그인용 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    //OAuth2 로그인용 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //사용자정보 반환
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> user.getUserRole());
    }

    @Override
    public String getPassword() {
        return user.getLoginInfo().getUserPwd();
    }

    @Override
    public String getUsername() {
        return user.getUserId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getUserName();
    }
}