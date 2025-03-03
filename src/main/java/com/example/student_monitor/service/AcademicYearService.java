package com.example.student_monitor.service;

import com.example.student_monitor.model.AcademicYear;
import com.example.student_monitor.repository.AcademicYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicYearService {

    @Autowired
    private AcademicYearRepository academicYearRepository;

    public List<AcademicYear> getAllAcademicYears() {
        return academicYearRepository.findAll();
    }

    public AcademicYear getAcademicYearById(Integer id) {
        return academicYearRepository.findById(id).orElse(null);
    }

    public void saveAcademicYear(AcademicYear academicYear) {
        academicYearRepository.save(academicYear);
    }

    public void deleteAcademicYearById(Integer id) {
        academicYearRepository.deleteById(id);
    }
}