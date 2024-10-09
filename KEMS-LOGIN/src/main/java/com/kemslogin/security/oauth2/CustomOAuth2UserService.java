package com.kemslogin.security.oauth2;

import com.kemslogin.user.model.domain.entity.LoginInfo;
import com.kemslogin.user.model.domain.entity.User;
import com.kemslogin.user.repository.LoginInfoRepository;
import com.kemslogin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final LoginInfoRepository loginInfoRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //구글 인증 후 유저 정보 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId(); // Google
        String providerId = oAuth2User.getAttribute("sub"); // Google의 사용자 ID
        String userEmail = oAuth2User.getAttribute("email");

        // User 엔티티를 통해 LoginInfo를 가져옴
        User user = userRepository.findByUserEmail(userEmail);

        if (user != null) {
            LoginInfo loginInfo = user.getLoginInfo();

            if (loginInfo != null) {
                //provider와 providerId 업데이트(Google 계정 연동)
                loginInfo.setProvider(provider);
                loginInfo.setProviderId(providerId);
                loginInfoRepository.save(loginInfo); //정보 업데이트
            } else {
                throw new IllegalStateException("해당 사용자의 로그인 정보가 존재하지 않습니다.");
            }

            //기존 유저 정보를 기반으로 CustomUserDetails 생성
            return new PrincipalDetails(user, oAuth2User.getAttributes());
        } else {
            throw new IllegalStateException("해당 이메일로 등록된 사용자가 존재하지 않습니다.");
        }
    }
}