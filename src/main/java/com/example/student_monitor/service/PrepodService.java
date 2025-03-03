package com.example.student_monitor.service;

import com.example.student_monitor.model.Prepod;
import com.example.student_monitor.model.User;
import com.example.student_monitor.repository.PrepodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PrepodService {
    @Autowired
    private PrepodRepository prepodRepository;

    @Autowired
    private UserService userService;

    public Prepod getById(Integer id) {
        return prepodRepository.findById(id).orElse(null);
    }

    public List<Prepod> getAll() {
        return prepodRepository.findAll();
    }

    public Optional<Prepod> findByPrepodIdNumber(String prepodIdNumber) {
        return prepodRepository.findByPrepodIdNumber(prepodIdNumber);
    }

    public long countByDepartmentId(Integer departmentId) {
        return prepodRepository.countByDepartmentId(departmentId);
    }

    public Prepod save(Prepod prepod) {
        return prepodRepository.save(prepod);
    }

    public void deleteById(Integer id) {
        prepodRepository.deleteById(id);
    }

    public Prepod getCurrentPrepod() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!user.getRole().getName().equals("PREPOD")) {
            throw new RuntimeException("Текущий пользователь не является преподавателем");
        }

        return user.getPrepod();
    }
}