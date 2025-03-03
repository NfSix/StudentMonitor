package com.example.student_monitor.controller;

import com.example.student_monitor.model.AcademicYear;
import com.example.student_monitor.model.Grade;
import com.example.student_monitor.model.Group;
import com.example.student_monitor.model.Student;
import com.example.student_monitor.service.GradeService;
import com.example.student_monitor.service.GroupService;
import com.example.student_monitor.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private GroupService groupService;

    // Список своих баллов
    @GetMapping("/grades")
    public String showGrades(Model model, Principal principal) {
        // Получаем текущего студента
        Student student = studentService.getCurrentStudent();
        // Получаем оценки студента, сгруппированные по учебным периодам
        Map<AcademicYear, List<Grade>> gradesByAcademicYear = gradeService.findGradesByStudentGroupedByAcademicYear(student);
        // Передаём
        model.addAttribute("student", student);
        model.addAttribute("gradesByAcademicYear", gradesByAcademicYear);

        return "student/grades";
    }

    // Отображение списка группы
    @GetMapping("/my-group")
    public String showMyGroup(Model model, Principal principal) {
        // Получаем текущего студента
        Student student = studentService.getCurrentStudent();
        // Получаем группу
        Group group = student.getGroup();
        // Получаем список студентов группы с дополнительно рассчитаным в сервисе средним баллом
        List<Student> students = groupService.getStudentsWithAverageGrade(group);
        // Передаём
        model.addAttribute("group", group);
        model.addAttribute("students", students);

        return "student/my-group";
    }
}