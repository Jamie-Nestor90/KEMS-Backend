package com.kemslogin.user.model.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Students")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_no", nullable = false)
    private Long studentNo;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true, nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(name = "student_regdate", nullable = false)
    private LocalDate regDate;

    @Builder
    public Student(User user, LocalDate regDate) {
        this.user = user;
        this.regDate = regDate;
    }

}