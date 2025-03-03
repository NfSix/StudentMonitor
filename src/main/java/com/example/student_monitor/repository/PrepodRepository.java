package com.example.student_monitor.repository;

import com.example.student_monitor.model.Prepod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PrepodRepository extends JpaRepository<Prepod, Integer> {
    List<Prepod> findByDepartmentId(Integer departmentId);
    Optional<Prepod> findByPrepodIdNumber(String prepodIdNumber);
    long countByDepartmentId(Integer departmentId);
}