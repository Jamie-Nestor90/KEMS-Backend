package com.kemslogin.user.controller;

import com.kemslogin.security.jwt.CustomUserDetails;
import com.kemslogin.security.jwt.CustomUserDetailsService;
import com.kemslogin.security.jwt.JwtUtil;
import com.kemslogin.security.oauth2.PrincipalDetails;
import com.kemslogin.user.model.domain.entity.LoginInfo;
import com.kemslogin.user.model.domain.entity.User;
import com.kemslogin.user.model.dto.LinkRequest;
import com.kemslogin.user.model.dto.LoginRequest;
import com.kemslogin.user.repository.LoginInfoRepository;
import com.kemslogin.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/user")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private PrincipalDetails principalDetails;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            //로그인 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getUserPwd())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtUtil.createAccessToken(customUserDetails.getUsername(), customUserDetails.getRole(), customUserDetails.getUserType(), 60*60*10L);

            //사용자 ID 및 관련 정보 반환
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("userId", customUserDetails.getUsername());
            responseBody.put("userName", customUserDetails.getUserName());
            responseBody.put("userDept", customUserDetails.getUserDept());
            responseBody.put("userRole", customUserDetails.getRole());
            responseBody.put("token", token);

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " + e.getMessage());
        }
    }

    @PostMapping("/link/google")
    public ResponseEntity<?> linkGoogle(@RequestBody LinkRequest linkRequest) {
        User user = userRepository.findByUserId(linkRequest.getUserId());
        if (user == null) {
            return ResponseEntity.badRequest().body("사용자가 존재하지 않습니다.");
        }

        LoginInfo loginInfo = loginInfoRepository.findByUserUserId(user.getUserId());
        if (loginInfo == null && loginInfo.getProvider() != null && loginInfo.getProviderId() != null) {
            return ResponseEntity.badRequest().body("이미 Google 계정이 연동되어 있습니다.");
        }

        //구글 계정 연동
        loginInfo.setProvider("");
        loginInfo.setProviderId("");
        loginInfoRepository.save(loginInfo);

        return ResponseEntity.ok("Google 계정이 성공적으로 연동되었습니다.");
    }

}