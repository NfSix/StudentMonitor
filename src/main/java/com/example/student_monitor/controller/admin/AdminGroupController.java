package com.example.student_monitor.controller.admin;

import com.example.student_monitor.model.*;
import com.example.student_monitor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/groups")
public class AdminGroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PrepodService prepodService;
    @Autowired
    private AcademicYearService academicYearService;
    @Autowired
    private SubjectPeriodService subjectPeriodService;
    @Autowired
    private PrepodSubjectGroupService prepodSubjectGroupService;

    // Отображение списка групп
    @GetMapping
    public String listGroups(Model model) {
        List<Group> groups = groupService.findAll();

        // Проверка на возможность удалить без нарушения ссылочной целостности
        Map<Integer, Boolean> canDeleteGroup = new HashMap<>();
        for (Group group : groups) {
            boolean canDelete = prepodSubjectGroupService.countByGroupId(group.getId()) == 0 &&
                    studentService.countByGroupId(group.getId()) == 0;
            canDeleteGroup.put(group.getId(), canDelete);
        }

        // Передаем данные в модель
        model.addAttribute("groups", groups);
        model.addAttribute("group", new Group());
        model.addAttribute("canDeleteGroup", canDeleteGroup);

        return "admin/groups";
    }

    // Сохранение новой группы
    @PostMapping
    public String saveGroup(@ModelAttribute("group") Group group) {
        groupService.save(group);
        return "redirect:/admin/groups";
    }

    // Форма редактирования
    @GetMapping("/edit/{id}")
    public String editGroupForm(@PathVariable Integer id, Model model) {
        Group group = groupService.getGroupById(id);
        if (group == null) {
            return "redirect:/admin/groups";
        }
        model.addAttribute("group", group);
        return "admin/groups";
    }

    // Обновление данных группы
    @PostMapping("/{id}")
    public String updateGroup(@PathVariable Integer id, @ModelAttribute("group") Group group) {
        group.setId(id); // Устанавливаем ID группы
        groupService.update(group);
        return "redirect:/admin/groups";
    }


    // Удаление группы
    @GetMapping("/delete/{id}")
    public String deleteGroup(@PathVariable Integer id) {
        groupService.deleteById(id);
        return "redirect:/admin/groups";
    }


    // Отображение страницы с предметами
    @GetMapping("/{groupId}")
    public String showGroupDetails(@PathVariable Integer groupId, Model model) {

        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            return "redirect:/admin/groups";
        }
        // Получаем все семестры
        List<AcademicYear> academicYears = academicYearService.getAllAcademicYears();
        // Получаем все связи PrepodSubjectGroup для данной группы
        List<PrepodSubjectGroup> prepodSubjectGroups = prepodSubjectGroupService.getPrepodSubjectGroupsByGroupId(groupId);
        // Получаем список преподавателей
        List<Prepod> prepods = prepodService.getAll();

        Map<Integer, Long> subjectCountByAcademicYear = new HashMap<>();
        for (AcademicYear academicYear : academicYears) {
            long count = subjectPeriodService.countByAcademicYearId(academicYear.getId());
            subjectCountByAcademicYear.put(academicYear.getId(), count);
        }

        // Передаем данные
        model.addAttribute("group", group);
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("subjectCountByAcademicYear", subjectCountByAcademicYear);
        model.addAttribute("prepodSubjectGroups", prepodSubjectGroups);
        model.addAttribute("prepods", prepods);

        return "admin/group-details";
    }

    // Добавление семестра
    @PostMapping("/academic-years")
    public String addAcademicYear(@RequestParam String name, @RequestParam Integer groupId) {
        AcademicYear academicYear = new AcademicYear();
        academicYear.setName(name);
        academicYearService.saveAcademicYear(academicYear);
        return "redirect:/admin/groups/" + groupId; // Перенаправляем обратно на страницу групп
    }


    // Удаление семестра
    @GetMapping("/{groupId}/academic-years/delete/{academicYearId}")
    public String deleteAcademicYear(
            @PathVariable Integer groupId,
            @PathVariable Integer academicYearId
    ) {
        // Получаем учебный период по ID
        AcademicYear academicYear = academicYearService.getAcademicYearById(academicYearId);

        // Проверяем, есть ли связанные предметы, так как удалить можно только пустые семестры
        if (subjectPeriodService.countByAcademicYearId(academicYearId) == 0) {
            academicYearService.deleteAcademicYearById(academicYearId);
        } else {
            throw new IllegalStateException("Невозможно удалить учебный период: есть связанные предметы.");
        }

        return "redirect:/admin/groups/" + groupId;
    }

    // Добавить предмет в учебный период группы
    @PostMapping("/{groupId}/subjects")
    public String addSubject(
            @PathVariable Integer groupId, // ID группы
            @RequestParam String name,     // Название предмета
            @RequestParam Integer academicYearId // ID учебного периода
    ) {
        // Создаем новый предмет
        SubjectPeriod subjectPeriod = new SubjectPeriod();
        subjectPeriod.setSubject(name);

        // Находим учебный период
        AcademicYear academicYear = academicYearService.getAcademicYearById(academicYearId);
        subjectPeriod.setAcademicYear(academicYear);

        // Сохраняем предмет
        SubjectPeriod savedSubjectPeriod = subjectPeriodService.saveSubjectPeriod(subjectPeriod);

        // Находим группу
        Group group = groupService.getGroupById(groupId);

        // Создаем связь в prepod_subject_group
        PrepodSubjectGroup prepodSubjectGroup = new PrepodSubjectGroup();
        prepodSubjectGroup.setSubjectPeriod(savedSubjectPeriod);
        prepodSubjectGroup.setGroup(group);
        prepodSubjectGroupService.savePrepodSubjectGroup(prepodSubjectGroup);

        return "redirect:/admin/groups/" + groupId;
    }

    // Удаление предмета реализовано на уровне БД каскадным удалением
    @GetMapping("/{groupId}/prepod-subject-groups/delete/{psgId}")
    public String deletePrepodSubjectGroup(
            @PathVariable Integer groupId, // ID группы
            @PathVariable Integer psgId // ID связи PrepodSubjectGroup
    ) {
        // Удаляем связь
        prepodSubjectGroupService.deletePrepodSubjectGroupById(psgId);

        // Редирект на страницу группы
        return "redirect:/admin/groups/" + groupId;
    }

    // Назначение преподавателя
    @PostMapping("/prepod-subject-groups/{psgId}/assign-prepod")
    public ResponseEntity<?> assignPrepod(@PathVariable Integer psgId, @RequestBody Map<String, Integer> request) {
        Integer prepodId = request.get("prepodId");
        prepodSubjectGroupService.assignPrepod(psgId, prepodId);
        return ResponseEntity.ok().build();
    }


}