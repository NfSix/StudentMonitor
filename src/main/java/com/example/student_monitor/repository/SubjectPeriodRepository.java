package com.example.student_monitor.repository;

import com.example.student_monitor.model.SubjectPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubjectPeriodRepository extends JpaRepository<SubjectPeriod, Integer> {
    List<SubjectPeriod> findByAcademicYearId(Integer academicYearId);
    long countByAcademicYearId(Integer academicYearId);
}
