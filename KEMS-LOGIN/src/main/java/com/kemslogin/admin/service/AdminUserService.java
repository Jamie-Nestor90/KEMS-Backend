package com.kemslogin.admin.service;

import com.kemslogin.user.model.domain.entity.*;
import com.kemslogin.user.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class AdminUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public User createUser(User user, String userPwd, LocalDate joinDate) {
        //이메일 중복 검사
        if (userRepository.existsByUserEmail(user.getUserEmail())) {
            throw new IllegalArgumentException("이메일이 이미 존재합니다.");
        }

        //전화번호 중복 검사
        if (userRepository.existsByUserPhone(user.getUserPhone())) {
            throw new IllegalArgumentException("전화번호가 이미 존재합니다.");
        }

        //User Entity에 우선 저장
        User savedUser = userRepository.save(user);

        //LoginInfo Entity에 저장
        LoginInfo loginInfo = LoginInfo.builder()
                .user(savedUser)
                .userPwd(userPwd)
                .userType(user.getUserType())
                .userRole(user.getUserRole())
                .build();
        loginInfoRepository.save(loginInfo);

        //UserType이 "학생"인 경우 Student Entity에 저장
        if ("학생".equals(user.getUserType())) {
            Student student = Student.builder()
                    .user(savedUser)
                    .regDate(joinDate)
                    .build();
            studentRepository.save(student);
        }
        //UserType이 "강사"인 경우 Tutor Entity에 저장
        else if ("강사".equals(user.getUserType())) {
            Tutor tutor = Tutor.builder()
                    .user(savedUser)
                    .hireDate(joinDate)
                    .build();
            tutorRepository.save(tutor);
        }

        //UserType이 "행정팀"인 경우 Employee Entity에 저장
        else if ("행정팀".equals(user.getUserType())) {
            Employee employee = Employee.builder()
                    .user(savedUser)
                    .hireDate(joinDate)
                    .build();
            employeeRepository.save(employee);
        }

        return savedUser;

    }

}