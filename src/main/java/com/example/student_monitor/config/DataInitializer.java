package com.example.student_monitor.config;

import com.example.student_monitor.model.Role;
import com.example.student_monitor.model.User;
import com.example.student_monitor.service.RoleService;
import com.example.student_monitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {  // Класс инициализации ролей и пользователя admin

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Создаём роли, если они ещё не созданы
            Role adminRole = roleService.findByName("ADMIN").orElseGet(() -> {
                Role role = new Role();
                role.setName("ADMIN");
                return roleService.save(role);
            });

            Role prepodRole = roleService.findByName("PREPOD").orElseGet(() -> {
                Role role = new Role();
                role.setName("PREPOD");
                return roleService.save(role);
            });

            Role studentRole = roleService.findByName("STUDENT").orElseGet(() -> {
                Role role = new Role();
                role.setName("STUDENT");
                return roleService.save(role);
            });

            // Создаём пользователя admin, если он ещё не создан
            if (userService.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin")); // Шифруем пароль
                admin.setRole(adminRole);
                userService.save(admin);
            }
        };
    }
}