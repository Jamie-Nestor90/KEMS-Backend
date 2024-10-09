package com.kemslogin.user.model.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Login_Info")
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "loginNo")
public class LoginInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_no", unique = true, nullable = false)
    private Long loginNo;

    @OneToOne
    @JoinColumn(name = "login_id", unique = true, nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "user_type", nullable = false)
    private String userType;

    @Column(name = "user_role", nullable = false)
    private String userRole;

    @Builder
    public LoginInfo(User user, String userPwd, String provider, String providerId, String userType, String userRole) {
        this.user = user;
        this.userPwd = userPwd;
        this.provider = provider;
        this.providerId = providerId;
        this.userType = userType;
        this.userRole = userRole;
    }

}