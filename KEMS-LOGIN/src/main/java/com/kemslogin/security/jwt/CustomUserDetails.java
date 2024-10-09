package com.kemslogin.security.jwt;

import com.kemslogin.user.model.domain.entity.LoginInfo;
import com.kemslogin.user.model.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final LoginInfo loginInfo;

    //User의 권한을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(() -> loginInfo.getUserRole());
        return collection;
    }

    //User의 ID를 반환
    @Override
    public String getUsername() {
        return loginInfo.getUser().getUserId().toString();
    }

    //User의 비밀번호를 반환
    @Override
    public String getPassword() {
        return loginInfo.getUserPwd();
    }

    //User의 이름을 반환
    public String getUserName() {
        return loginInfo.getUser().getUserName();
    }

    //User의 권한을 반환
    public String getRole() {
        return loginInfo.getUserRole();
    }

    //User의 타입을 반환
    public String getUserType() {
        return loginInfo.getUserType();
    }

    //User의 부서 반환
    public String getUserDept() {
        return loginInfo.getUser().getUserDept();
    }

    //User 객체를 반환
    public User getUser() {
        return loginInfo.getUser();
    }

    //LoginInfo 객체를 반환
    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    //계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠김 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //자격증명 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성화 여부 반환
    @Override
    public boolean isEnabled() {
        return true;
    }

}