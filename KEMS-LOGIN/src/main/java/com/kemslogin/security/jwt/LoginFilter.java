package com.kemslogin.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kemslogin.user.model.dto.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    //LoginFilter 생성자
    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, String loginUrl) {
        setFilterProcessesUrl(loginUrl);
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        System.out.println("LoginFilter #1");
    }

    //로그인 시도 요청
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            System.out.println("LoginFilter#1 / 로그인 요청 확인 " + request + "," + response);
            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            //로그인 요청 정보 출력 (디버깅용)
            System.out.println("LoginFilter#1.1 / 로그인 요청 정보: " + loginRequest.getUserId() + ", " + loginRequest.getUserPwd());

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserId(),
                            loginRequest.getUserPwd(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            System.out.println("LoginFilter#2/attemptAuthentication/IOException: 로그인 요청 확인 실패");
            throw new RuntimeException(e);
        } catch (AuthenticationException e) {
            System.out.println("LoginFilter#3/attemptAuthentication/AuthenticationException: 로그인 요청 확인 실패");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"자격 증명에 실패하였습니다.\"}");
                response.getWriter().flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw e;
        }
    }

    //로그인 성공시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = customUserDetails.getUsername();
        String userRole = customUserDetails.getAuthorities().iterator().next().getAuthority();
        String userType = customUserDetails.getUserType();
        String userDept = customUserDetails.getUserDept();
        String token = jwtUtil.createAccessToken(userId, userRole, userType, 60 * 60 * 10L);

        System.out.println("LoginFilter/로그인 성공 " + userId + " " + userRole + " " + token);

        //JWT 토큰을 헤더에 추가
        response.addHeader("Authorization", "Bearer " + token);

        //응답 본문에 포함할 데이터 생성
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", userId);
        responseBody.put("userRole", userRole);
        responseBody.put("userType", userType);
        responseBody.put("userDept", userDept);
        responseBody.put("userName", customUserDetails.getUser().getUserName());

        //응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //ObjectMapper를 사용하여 Map을 JSON 문자열로 변환
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(responseBody);

        //JSON 응답을 본문에 작성
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    //로그인 실패시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        System.out.println("LoginFilter/로그인 실패 #1");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            System.out.println("LoginFilter/로그인 실패 #2");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\":\"자격 증명에 실패하였습니다.\"}");
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}