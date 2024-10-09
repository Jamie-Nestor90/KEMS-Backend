package com.kemslogin.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //JWT 토큰에서 사용자 이름 가져오기
    public String getUsername(String token) {
        return Jwts.parser().build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    //JWT 토큰에서 사용자 역할 가져오기
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    //JWT 토큰에서 만료여부 확인
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    //JWT AccessToken 생성
    public String createAccessToken(String username, String userType, String role, Long expiredSeconds) {
        long expiredMs = expiredSeconds * 1000;
        System.out.println("Util/expiredMs: " + expiredMs);
        return Jwts.builder()
                .claim("username", username)
                .claim("userType", userType)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}