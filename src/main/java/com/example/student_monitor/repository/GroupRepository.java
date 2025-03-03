package com.example.student_monitor.repository;

import com.example.student_monitor.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findByName(String name);
}