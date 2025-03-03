
package com.example.student_monitor.service;

import com.example.student_monitor.model.Department;
import com.example.student_monitor.model.Prepod;
import com.example.student_monitor.model.Student;
import com.example.student_monitor.model.User;
import com.example.student_monitor.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Integer id) {
        return studentRepository.findById(id);
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    public void editStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudent(Integer id) {
        studentRepository.deleteById(id);
    }

    public List<Student> findByGroupId(Integer groupId) {
        return studentRepository.findByGroupId(groupId);
    }

    public Optional<Student> findByStudentIdNumber(String studentIdNumber) {
        return studentRepository.findByStudentIdNumber(studentIdNumber);
    }

    public long countByGroupId(Integer groupId) {
        return studentRepository.countByGroupId(groupId);
    }

    public Student getCurrentStudent() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!user.getRole().getName().equals("STUDENT")) {
            throw new RuntimeException("Текущий пользователь не является студентом");
        }

        return user.getStudent();
    }
}