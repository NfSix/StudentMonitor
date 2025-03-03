package com.example.student_monitor.service;

import com.example.student_monitor.model.Group;
import com.example.student_monitor.model.Student;
import com.example.student_monitor.model.Grade;
import com.example.student_monitor.repository.GradeRepository;
import com.example.student_monitor.repository.GroupRepository;
import com.example.student_monitor.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    public Optional<Group> findById(Integer id) {
        return groupRepository.findById(id);
    }

    public Group getGroupById(Integer id) {
        return groupRepository.findById(id).orElse(null);
    }

    public Group save(Group group) {
        return groupRepository.save(group);
    }

    public void update(Group group) {
        groupRepository.save(group);
    }

    public void deleteById(Integer id) {
        groupRepository.deleteById(id);
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public List<Student> getStudentsWithAverageGrade(Group group) {
        List<Student> students = studentRepository.findByGroupId(group.getId());
        students.forEach(student -> {
            Double averageGrade = calculateAverageGrade(student);
            student.setAverageGrade(averageGrade);
        });
        return students;
    }

    // рассчет среднего балла по тем предметам, по которым баллы уже выставлены
    private Double calculateAverageGrade(Student student) {
        List<Grade> grades = gradeRepository.findByStudent(student);
        if (grades.isEmpty()) {
            return null;
        }
        double total = grades.stream()
                .mapToInt(grade -> (grade.getScoreSemester() != null ? grade.getScoreSemester() : 0) +
                        (grade.getScoreExam() != null ? grade.getScoreExam() : 0))
                .sum();
        return total / grades.size();
    }
}