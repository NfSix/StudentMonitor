package com.example.student_monitor.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
    // Отображение страницы по умолчанию
    @GetMapping("/")
    public String defaultAfterLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // Получаем данные о текущем пользователе
        String role = auth.getAuthorities().iterator().next().getAuthority(); // Получаем роль
        String layout;
        switch (role) {
            case "ROLE_ADMIN":
                layout = "redirect:/admin/students"; // Стартовая страница админа
                break;
            case "ROLE_STUDENT":
                layout = "redirect:/student/grades"; // Стартовая страница студента
                break;
            case "ROLE_PREPOD":
                layout = "redirect:/prepod/subjects"; // Стартовая страница преподавателя
                break;
            default:
                throw new IllegalStateException("Неизвестная роль: " + role);
        }
        return layout;
    }

}