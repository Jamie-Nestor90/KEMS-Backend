package com.kemslogin.user.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String userName;
    private String userType;
    private String userRole;
    private String token;
}