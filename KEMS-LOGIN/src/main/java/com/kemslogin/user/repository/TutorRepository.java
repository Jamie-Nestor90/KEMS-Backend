package com.kemslogin.user.repository;

import com.kemslogin.user.model.domain.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
}