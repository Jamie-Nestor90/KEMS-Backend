package com.kemslogin.admin.controller;

import com.kemslogin.admin.model.dto.CreateUserRequest;
import com.kemslogin.admin.service.AdminUserService;
import com.kemslogin.user.model.domain.entity.User;
import com.kemslogin.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {

    @Autowired
    private final AdminUserService adminUserService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserRepository userRepository;

    public AdminController(AdminUserService adminUserService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.adminUserService = adminUserService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/user/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        //비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(createUserRequest.getUserPwd());

        //사용자 엔티티 생성
        User user = User.builder()
                .userId(createUserRequest.getUserId())
                .userName(createUserRequest.getUserName())
                .userEmail(createUserRequest.getUserEmail())
                .userPhone(createUserRequest.getUserPhone())
                .userDept(createUserRequest.getUserDept())
                .userType(createUserRequest.getUserType())
                .userRole(createUserRequest.getUserRole())
                .build();

        //입학/입사일(joinDate) 처리
        LocalDate joinDate = createUserRequest.getJoinDate();

        //사용자 저장
        User savedUser = adminUserService.createUser(user, encryptedPassword, joinDate);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

}