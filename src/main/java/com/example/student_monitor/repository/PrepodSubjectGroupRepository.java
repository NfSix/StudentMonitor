package com.example.student_monitor.repository;


import com.example.student_monitor.model.PrepodSubjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PrepodSubjectGroupRepository extends JpaRepository<PrepodSubjectGroup, Integer> {
    List<PrepodSubjectGroup> findByPrepodId(Integer prepodId);
    List<PrepodSubjectGroup> findByGroupId(Integer groupId);
    List<PrepodSubjectGroup> findBySubjectPeriodId(Integer subjectPeriodId);
    Optional<PrepodSubjectGroup> findByGroupIdAndSubjectPeriodId(Integer groupId, Integer subjectPeriodId);
    long countBySubjectPeriodId(Integer subjectPeriodId);
    long countByPrepodId(Integer prepodId);
    long countByGroupId(Integer groupId);

}