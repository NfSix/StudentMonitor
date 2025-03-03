package com.example.student_monitor.repository;

import com.example.student_monitor.model.Grade;
import com.example.student_monitor.model.Student;
import com.example.student_monitor.model.SubjectPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    Optional<Grade> findByStudentAndSubjectPeriod(Student student, SubjectPeriod subjectPeriod);
    List<Grade> findByStudent(Student student);
}