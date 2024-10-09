package com.kemslogin.user.repository;

import com.kemslogin.user.model.domain.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
    LoginInfo findByUserUserId(Long userId);
}