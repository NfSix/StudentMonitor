package com.example.student_monitor.config;

import com.example.student_monitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    // Настройка SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll() // Доступ для всех
                        .requestMatchers("/student/**").hasRole("STUDENT") // Доступ для студентов
                        .requestMatchers("/prepod/**").hasRole("PREPOD") // Доступ для преподавателей
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Доступ для администраторов
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Страница входа
                        .defaultSuccessUrl("/") // Редирект после успешного входа
                        .permitAll()
                )
                .logout(logout -> logout // Настройка выхода
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login") // Редирект после выхода
                        .permitAll()
                );
        return http.build();
    }

    // Класс аутентификации при помощи UserService
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Шифровщик паролей для безопасного их хранения в БД
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}