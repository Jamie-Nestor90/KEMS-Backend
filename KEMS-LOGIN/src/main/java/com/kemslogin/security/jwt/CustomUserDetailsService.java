package com.kemslogin.security.jwt;

import com.kemslogin.user.model.domain.entity.LoginInfo;
import com.kemslogin.user.repository.LoginInfoRepository;
import com.kemslogin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    //User 정보 불러오기
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginInfo loginInfo = loginInfoRepository.findByUserUserId(Long.parseLong(username));
        if (loginInfo == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username);
        }

        return new CustomUserDetails(loginInfo);
    }

}