package com.example.student_monitor.repository;

import com.example.student_monitor.model.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer> {
}
