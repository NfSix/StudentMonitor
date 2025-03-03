package com.example.student_monitor.service;

import com.example.student_monitor.model.Prepod;
import com.example.student_monitor.model.PrepodSubjectGroup;
import com.example.student_monitor.repository.PrepodRepository;
import com.example.student_monitor.repository.PrepodSubjectGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrepodSubjectGroupService {

    @Autowired
    private PrepodSubjectGroupRepository prepodSubjectGroupRepository;

    @Autowired
    private PrepodRepository prepodRepository;

    @Autowired
    private SubjectPeriodService subjectPeriodService;

    public void savePrepodSubjectGroup(PrepodSubjectGroup prepodSubjectGroup) {
        prepodSubjectGroupRepository.save(prepodSubjectGroup);
    }

    public long countByPrepodId(Integer prepodId) {
        return prepodSubjectGroupRepository.countByPrepodId(prepodId);
    }

    public long countByGroupId(Integer groupId) {
        return prepodSubjectGroupRepository.countByGroupId(groupId);
    }

    public void deletePrepodSubjectGroupById(Integer id) {
        PrepodSubjectGroup psg = prepodSubjectGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Связь не найдена"));
        prepodSubjectGroupRepository.deleteById(id);
        //"Каскадное удаление" предмета в таблице subject_period, если больше нет связей
        if (prepodSubjectGroupRepository.countBySubjectPeriodId(psg.getSubjectPeriod().getId()) == 0) {
            subjectPeriodService.deleteSubjectPeriodById(psg.getSubjectPeriod().getId());
        }
    }

    public List<PrepodSubjectGroup> getPrepodSubjectGroupsByGroupId(Integer groupId) {
        return prepodSubjectGroupRepository.findByGroupId(groupId);
    }

    public List<PrepodSubjectGroup> getPrepodSubjectGroupsByPrepodId(Integer prepodId) {
        return prepodSubjectGroupRepository.findByPrepodId(prepodId);
    }

    public void assignPrepod(Integer psgId, Integer prepodId) {
        PrepodSubjectGroup psg = prepodSubjectGroupRepository.findById(psgId)
                .orElseThrow(() -> new RuntimeException("Связь не найдена"));
        Prepod prepod = prepodRepository.findById(prepodId)
                .orElseThrow(() -> new RuntimeException("Преподаватель не найден"));

        psg.setPrepod(prepod);
        prepodSubjectGroupRepository.save(psg);
    }
}