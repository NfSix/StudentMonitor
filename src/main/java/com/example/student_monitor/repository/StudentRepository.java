package com.example.student_monitor.repository;

import java.util.List;

import com.example.student_monitor.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByStudentIdNumber(String studentIdNumber);
    List<Student> findByGroupId(Integer groupId);
    long countByGroupId(Integer groupId);
}
