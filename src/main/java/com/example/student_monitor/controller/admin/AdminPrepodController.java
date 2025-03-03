package com.example.student_monitor.controller.admin;

import com.example.student_monitor.model.Prepod;
import com.example.student_monitor.service.DepartmentService;
import com.example.student_monitor.service.PrepodService;
import com.example.student_monitor.service.PrepodSubjectGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/prepods")
public class AdminPrepodController {

    @Autowired
    private PrepodService prepodService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PrepodSubjectGroupService prepodSubjectGroupService;

    // Отображение списка преподавателей
    @GetMapping
    public String listPrepods(Model model) {
        List<Prepod> prepods = prepodService.getAll();

        // Проверка на возможность удалить без нарушения ссылочной целостности
        Map<Integer, Boolean> canDeletePrepod = new HashMap<>();
        for (Prepod prepod : prepods) {
            boolean canDelete = prepodSubjectGroupService.countByPrepodId(prepod.getId()) == 0;
            canDeletePrepod.put(prepod.getId(), canDelete);
        }

        // Передаем данные в модель
        model.addAttribute("prepods", prepods);
        model.addAttribute("prepod", new Prepod());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("canDeletePrepod", canDeletePrepod);

        return "admin/prepods";
    }

    // Сохранение нового преподавателя
    @PostMapping
    public String savePrepod(@ModelAttribute("prepod") Prepod prepod) {
        prepodService.save(prepod);
        return "redirect:/admin/prepods";
    }

    //Форма редактирования
    @GetMapping("/edit/{id}")
    public String editPrepodForm(@PathVariable Integer id, Model model) {
        Prepod prepod = prepodService.getById(id);
        if (prepod == null) {
            return "redirect:/admin/prepods";
        }
        // Передаем преподавателя в модель
        model.addAttribute("prepod", prepod);
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "admin/prepods";
    }

    // Редактирование преподавателя
    @PostMapping("/{id}")
    public String updatePrepod(@PathVariable Integer id, @ModelAttribute("prepod") Prepod prepod) {
        prepod.setId(id);
        prepodService.save(prepod);
        return "redirect:/admin/prepods";
    }

    // Удаление преподавателя
    @GetMapping("/delete/{id}")
    public String deletePrepod(@PathVariable Integer id) {
        prepodService.deleteById(id);
        return "redirect:/admin/prepods";
    }
}