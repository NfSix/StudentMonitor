package com.example.student_monitor.service;

import com.example.student_monitor.model.AcademicYear;
import com.example.student_monitor.model.Grade;
import com.example.student_monitor.model.Student;
import com.example.student_monitor.model.SubjectPeriod;
import com.example.student_monitor.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public Optional<Grade> findByStudentAndSubjectPeriod(Student student, SubjectPeriod subjectPeriod) {
        return gradeRepository.findByStudentAndSubjectPeriod(student, subjectPeriod);
    }


    public void save(Grade grade) {
        gradeRepository.save(grade);
    }

    public Map<AcademicYear, List<Grade>> findGradesByStudentGroupedByAcademicYear(Student student) {
        List<Grade> grades = gradeRepository.findByStudent(student);
        return grades.stream()
                .collect(Collectors.groupingBy(grade -> grade.getSubjectPeriod().getAcademicYear()));
    }
}