package com.kemslogin.user.repository;

import com.kemslogin.user.model.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(Long userId);

    User findByUserEmail(String userEmail);

    //이메일 중복 검사
    boolean existsByUserEmail(String userEmail);

    //전화번호 중복 검사
    boolean existsByUserPhone(String userPhone);

}