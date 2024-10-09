package com.kemslogin.security.jwt;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String userId;
    private String userName;
    private String userType;
    private String userRole;
    private String token;
    private Long entityNo;
}