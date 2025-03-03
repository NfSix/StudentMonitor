package com.example.student_monitor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_period_id")
    private SubjectPeriod subjectPeriod;

    private Integer scoreSemester;
    private Integer scoreExam;
    private String gradeStatus;
}