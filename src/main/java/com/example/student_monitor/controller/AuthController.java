package com.example.student_monitor.controller;

import com.example.student_monitor.model.Prepod;
import com.example.student_monitor.model.Role;
import com.example.student_monitor.model.Student;
import com.example.student_monitor.model.User;
import com.example.student_monitor.service.PrepodService;
import com.example.student_monitor.service.RoleService;
import com.example.student_monitor.service.StudentService;
import com.example.student_monitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PrepodService prepodService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String uid,
            @RequestParam String role) {

        // Находим роль по имени
        Role userRole = roleService.findByName(role)
                .orElseThrow(() -> new RuntimeException("Роль не найдена: " + role));

        // Создаём пользователя
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(userRole);

        // Присваиваем роль в зависимости от выбора, но только при совпадении идентификатора
        if (role.equals("STUDENT")) {
            Student student = studentService.findByStudentIdNumber(uid)
                    .orElseThrow(() -> new RuntimeException("Неверный UID: " + uid));
            user.setStudent(student);
            student.setUser(user);
        } else if (role.equals("PREPOD")) {
            Prepod prepod = prepodService.findByPrepodIdNumber(uid)
                    .orElseThrow(() -> new RuntimeException("Неверный UID: " + uid));
            user.setPrepod(prepod);
            prepod.setUser(user);
        }

        userService.save(user);
        return "login";
    }
}