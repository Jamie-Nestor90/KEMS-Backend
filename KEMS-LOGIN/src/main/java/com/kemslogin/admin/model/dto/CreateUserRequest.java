package com.kemslogin.admin.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    //User Entity와 관련된 필드
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userDept;
    private String userType;
    private String userRole = "ROLE_USER"; // Default value

    //LoginInfo Entity와 관련된 필드
    private String userPwd;

    //UserType별 Entity와 관련된 필드
    private LocalDate joinDate;
}