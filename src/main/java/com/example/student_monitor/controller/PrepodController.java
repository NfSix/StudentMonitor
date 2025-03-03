package com.example.student_monitor.controller;

import com.example.student_monitor.model.*;
import com.example.student_monitor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/prepod")
public class PrepodController {

    @Autowired
    private PrepodService prepodService;

    @Autowired
    private PrepodSubjectGroupService prepodSubjectGroupService;

    @Autowired
    private SubjectPeriodService subjectPeriodService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GradeService gradeService;

    @GetMapping("/subjects")
    public String listSubjects(Model model) {
        // Получаем текущего преподавателя
        Prepod currentPrepod = prepodService.getCurrentPrepod();
        // Ищем все записи PrepodSubjectGroup для текущего преподавателя
        List<PrepodSubjectGroup> prepodSubjectGroups = prepodSubjectGroupService.getPrepodSubjectGroupsByPrepodId(currentPrepod.getId());
        // Группируем данные по учебным группам в цикле
        Map<Group, List<SubjectPeriod>> groupSubjectsMap = new HashMap<>();
        for (PrepodSubjectGroup psg : prepodSubjectGroups) {
            Group group = psg.getGroup();
            SubjectPeriod subjectPeriod = psg.getSubjectPeriod();
            // Добавляем предмет в список для данной группы
            groupSubjectsMap.computeIfAbsent(group, k -> new ArrayList<>()).add(subjectPeriod);
        }
        // Передаем данные
        model.addAttribute("groupSubjectsMap", groupSubjectsMap);
        return "prepod/subjects";
    }

    // Список студентов для проставления баллов
    @GetMapping("/subjects/{groupId}/{subjectPeriodId}")
    public String showStudentsForSubject(
            @PathVariable Integer groupId,
            @PathVariable Integer subjectPeriodId,
            Model model) {

        // Получаем список студентов группы
        List<Student> students = studentService.findByGroupId(groupId);
        // Получаем SubjectPeriod
        SubjectPeriod subjectPeriod = subjectPeriodService.getSubjectPeriodById(subjectPeriodId);
        // Получаем оценки для каждого студента
        Map<Student, Grade> studentGrades = new HashMap<>();
        for (Student student : students) {
            Grade grade = gradeService.findByStudentAndSubjectPeriod(student, subjectPeriod)
                    .orElse(new Grade());
            studentGrades.put(student, grade);
        }

        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Передаем
        model.addAttribute("students", students);
        model.addAttribute("subjectPeriod", subjectPeriod);
        model.addAttribute("studentGrades", studentGrades);
        model.addAttribute("groupId", groupId);
        model.addAttribute("groupName", group.getName());

        return "prepod/subject-students";
    }
    // Выставить баллы за семестр
    @PostMapping("/subjects/{groupId}/{subjectPeriodId}/save-semester")
    public String saveSemesterScore(
            @PathVariable Integer groupId,
            @PathVariable Integer subjectPeriodId,
            @RequestParam Integer studentId,
            @RequestParam Integer scoreSemester) {

        saveGradeField(studentId, subjectPeriodId, "scoreSemester", scoreSemester);
        return "redirect:/prepod/subjects/" + groupId + "/" + subjectPeriodId;
    }
    //Выставить баллы за экзамен или зачет
    @PostMapping("/subjects/{groupId}/{subjectPeriodId}/save-exam")
    public String saveExamScore(
            @PathVariable Integer groupId,
            @PathVariable Integer subjectPeriodId,
            @RequestParam Integer studentId,
            @RequestParam Integer scoreExam) {

        saveGradeField(studentId, subjectPeriodId, "scoreExam", scoreExam);
        return "redirect:/prepod/subjects/" + groupId + "/" + subjectPeriodId;
    }
    // Выставить итоговую оценку
    @PostMapping("/subjects/{groupId}/{subjectPeriodId}/save-status")
    public String saveGradeStatus(
            @PathVariable Integer groupId,
            @PathVariable Integer subjectPeriodId,
            @RequestParam Integer studentId,
            @RequestParam String gradeStatus) {

        saveGradeField(studentId, subjectPeriodId, "gradeStatus", gradeStatus);
        return "redirect:/prepod/subjects/" + groupId + "/" + subjectPeriodId;
    }

    // Метод определения, в каком поле проставляем баллы
    private void saveGradeField(Integer studentId, Integer subjectPeriodId, String field, Object value) {
        // Получаем Student и SubjectPeriod
        Student student = studentService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        SubjectPeriod subjectPeriod = subjectPeriodService.getSubjectPeriodById(subjectPeriodId);

        // Получаем или создаем оценку
        Grade grade = gradeService.findByStudentAndSubjectPeriod(student, subjectPeriod)
                .orElse(new Grade());

        grade.setStudent(student);
        grade.setSubjectPeriod(subjectPeriod);

        switch (field) {
            case "scoreSemester":
                grade.setScoreSemester((Integer) value);
                break;
            case "scoreExam":
                grade.setScoreExam((Integer) value);
                break;
            case "gradeStatus":
                grade.setGradeStatus((String) value);
                break;
        }

        gradeService.save(grade);
    }
}