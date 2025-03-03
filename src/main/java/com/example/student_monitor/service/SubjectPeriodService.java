package com.example.student_monitor.service;

import com.example.student_monitor.model.SubjectPeriod;
import com.example.student_monitor.repository.SubjectPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectPeriodService {

    @Autowired
    private SubjectPeriodRepository subjectPeriodRepository;

    public SubjectPeriod getSubjectPeriodById(Integer id) {
        return subjectPeriodRepository.findById(id).orElse(null);
    }

    public SubjectPeriod saveSubjectPeriod(SubjectPeriod subjectPeriod) {
        return subjectPeriodRepository.save(subjectPeriod);
    }

    public long countByAcademicYearId(Integer academicYearId) {
        return subjectPeriodRepository.countByAcademicYearId(academicYearId);
    }

    public void deleteSubjectPeriodById(Integer id) {
        subjectPeriodRepository.deleteById(id);
    }
}