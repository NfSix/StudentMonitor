package com.example.student_monitor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class SubjectPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String subject;

    @ManyToOne
    @JoinColumn(name = "academicYearId")
    private AcademicYear academicYear;

    @OneToMany(mappedBy = "subjectPeriod")
    private List<PrepodSubjectGroup> prepodSubjectGroups;

    @OneToMany(mappedBy = "subjectPeriod")
    private List<Grade> grades;
}