package com.example.student_monitor.controller.admin;

import com.example.student_monitor.model.Student;
import com.example.student_monitor.service.GroupService;
import com.example.student_monitor.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/students")
public class AdminStudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService;

    // Отображение списка студентов
    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.findAll());
        return "admin/students";
    }

    // Добавление студента
    @PostMapping
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.addStudent(student);
        return "redirect:/admin/students";
    }

    // Редактирование студента
    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Integer id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return "redirect:/admin/students";
        }
        model.addAttribute("student", student);
        model.addAttribute("groups", groupService.findAll()); // Передаем список групп
        return "admin/students";
    }

    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Integer id, @ModelAttribute("student") Student student) {
        student.setId(id);
        studentService.editStudent(student);
        return "redirect:/admin/students";
    }

    // Удаление студента
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return "redirect:/admin/students";
    }
}