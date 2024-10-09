package com.kemslogin.user.model.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "user_email", unique = true, nullable = false, length = 100)
    private String userEmail;

    @Column(name = "user_phone", nullable = false, length = 15)
    private String userPhone;

    @Column(name = "user_dept", nullable = false)
    private String userDept;

    @Column(name = "user_type", nullable = false)
    private String userType;

    @Column(name = "user_role", nullable = false)
    private String userRole;

    @Column(name = "user_createAt", nullable = false)
    private LocalDateTime userCreateAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private LoginInfo loginInfo;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student student;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Employee employee;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Tutor tutor;

    @Builder
    public User(Long userId, String userName, String userEmail, String userPhone, String userDept, String userType, String userRole, LoginInfo loginInfo) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userDept = userDept;
        this.userType = userType;
        this.userRole = userRole;
        this.loginInfo = loginInfo;
    }

    @PrePersist
    public void prePersist() {
        this.userCreateAt = LocalDateTime.now();
    }

}