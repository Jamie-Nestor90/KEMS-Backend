package com.kemslogin.user.repository;

import com.kemslogin.user.model.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}