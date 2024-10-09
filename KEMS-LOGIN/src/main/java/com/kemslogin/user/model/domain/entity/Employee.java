package com.kemslogin.user.model.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_no", unique = true, nullable = false)
    private Long empNo;

    @OneToOne
    @JoinColumn(name = "emp_id", unique = true, nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(name = "emp_hiredate", nullable = false)
    private LocalDate hireDate;

    @Builder
    public Employee(User user, LocalDate hireDate) {
        this.user = user;
        this.hireDate = hireDate;
    }

}