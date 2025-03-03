package com.example.student_monitor.controller;

import com.example.student_monitor.service.RoleService;
import com.example.student_monitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AboutController { // Окно Об авторе

    @Autowired
    private UserService userService;

    private RoleService roleService;

    @GetMapping("/about")
    public String about(Model model, Principal principal) {
            String role = userService.getUserRole(principal.getName()); // Роль передаётся для выбора шапки
            model.addAttribute("userRole", role);
            return "about";
    }
}