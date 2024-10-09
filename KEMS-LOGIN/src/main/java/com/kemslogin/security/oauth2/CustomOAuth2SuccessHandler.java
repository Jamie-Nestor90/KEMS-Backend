package com.kemslogin.security.oauth2;

import com.kemslogin.security.jwt.JwtUtil;
import com.kemslogin.user.model.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Autowired
    public CustomOAuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        System.out.println("CustomOAuth2SuccessHandler 진입");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        String token = jwtUtil.createAccessToken(user.getUserId().toString(), user.getUserType(), user.getUserRole(), 60 * 60 * 10L);

        System.out.println("OAuth2SuccessHandler/진입 성공 " + user.getUserId() + " " + user.getUserRole() + " " + token);

        //프론트엔드로 리디렉션하며 토큰과 사용자 정보를 전달
        String redirectUrl = "http://localhost:3000/oauth2/redirect"
                + "?token=" + URLEncoder.encode(token, "UTF-8")
                + "&userId=" + URLEncoder.encode(user.getUserId().toString(), "UTF-8")
                + "&userName=" + URLEncoder.encode(user.getUserName(), "UTF-8")
                + "&userRole=" + URLEncoder.encode(user.getUserRole(), "UTF-8")
                + "&userType=" + URLEncoder.encode(user.getUserType(), "UTF-8")
                + "&userDept=" + URLEncoder.encode(user.getUserDept(), "UTF-8");

        response.sendRedirect(redirectUrl);
    }
}