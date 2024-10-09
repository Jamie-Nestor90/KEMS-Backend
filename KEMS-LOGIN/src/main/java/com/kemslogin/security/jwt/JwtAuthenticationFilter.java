package com.kemslogin.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //Request에서 Authorization Header 가져오기
        String authorization = request.getHeader("Authorization");

        //Authorization Header 검증하기
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("Authorization header is missing or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Authorization header present, processing token");

        //Bearer 토큰 부분 추출
        String token = authorization.substring(7);

        //JWT 토큰 소멸시간 검증하기
        try {
            if (jwtUtil.isExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is expired");
                response.getWriter().flush();
                return;
            }
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is expired");
            response.getWriter().flush();
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Token");
            response.getWriter().flush();
            return;
        }

        //JWT 토큰에서 사용자명과 권한 추출
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //UserDetails에 회원정보 담기
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}