package com.kemslogin.user.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private Long userId;
    private String userPwd;
}