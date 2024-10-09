package com.kemslogin.user.repository;

import com.kemslogin.user.model.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
