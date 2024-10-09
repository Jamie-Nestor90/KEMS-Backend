package com.kemslogin.user.model.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Tutors")
@Getter
@Setter
@NoArgsConstructor
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tutor_no", unique = true, nullable = false)
    private Long tutorNo;

    @OneToOne
    @JoinColumn(name = "tutor_id", unique = true, nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(name = "tutor_hiredate", nullable = false)
    private LocalDate hireDate;

    @Column(name = "tutor_career")
    private String tutorCareer;

    @Column(name = "tutor_certificate")
    private String tutorCertificate;

    @Builder
    public Tutor(User user, LocalDate hireDate, String tutorCareer, String tutorCertificate) {
        this.user = user;
        this.hireDate = hireDate;
        this.tutorCareer = tutorCareer;
        this.tutorCertificate = tutorCertificate;
    }

}