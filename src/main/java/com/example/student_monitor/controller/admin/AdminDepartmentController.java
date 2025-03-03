package com.example.student_monitor.controller.admin;

import com.example.student_monitor.model.Department;
import com.example.student_monitor.service.DepartmentService;
import com.example.student_monitor.service.PrepodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/departments")
public class AdminDepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PrepodService prepodService;

    // Отображение списка факультетов
    @GetMapping
    public String listDepartments(Model model) {
        List<Department> departments = departmentService.getAllDepartments();
        // Проверка на возможность удалить без нарушения ссылочной целостности
        Map<Integer, Boolean> canDeleteDepartment = new HashMap<>();
        for (Department department : departments) {
            boolean canDelete = prepodService.countByDepartmentId(department.getId()) == 0;
            canDeleteDepartment.put(department.getId(), canDelete);
        }

        // Передаем данные в модель
        model.addAttribute("departments", departments);
        model.addAttribute("department", new Department());
        model.addAttribute("canDeleteDepartment", canDeleteDepartment);

        return "admin/departments";
    }

    // Сохранение нового факультета
    @PostMapping
    public String saveDepartment(@ModelAttribute("department") Department department) {
        departmentService.saveDepartment(department);
        return "redirect:/admin/departments";
    }

    // Форма редактирования факультета
    @GetMapping("/edit/{id}")
    public String editDepartmentForm(@PathVariable Integer id, Model model) {
        // Находим факультет по ID
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            // Если факультета не существует, редирект на список факультетов
            return "redirect:/admin/departments";
        }

        // Передаем факультет в модель
        model.addAttribute("department", department);
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "admin/departments";
    }

    // Обновление данных факультета
    @PostMapping("/{id}")
    public String updateDepartment(@PathVariable Integer id, @ModelAttribute("department") Department department) {
        department.setId(id);
        departmentService.updateDepartment(department);
        return "redirect:/admin/departments";
    }

    // Удаление факультета
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartmentById(id);
        return "redirect:/admin/departments";
    }
}