package com.example.school_diary_end_project.services;

import com.example.school_diary_end_project.entities.ScheduleEntity;
import com.example.school_diary_end_project.entities.dto.GradeDto;
import com.example.school_diary_end_project.entities.enums.EGradeType;
import com.example.school_diary_end_project.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationMethodsImpl implements ValidationMethods {

    @Autowired
    private ScheduleRepository schedule;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private SubjectRepository subRepo;

    @Autowired
    private DepartmentRepository depRepo;

    @Autowired
    private PupilRepository pupilRepo;

    @Autowired
    private GradeRepository gradeRepo;

    /**
     * Here its not needed to have existByTeacher* from grade repository as the teacher may change during the period of semmester but we asume db will be up to date at that point
     * @param grade
     * @return
     */

    public Boolean checkForGrade(GradeDto grade) {
        if (schedule.existsByTeacherAndSubjectAndDepartment(teacherRepo.findById(grade.getProfessorId()).get(),
                subRepo.findById(grade.getSubjectId()).get(),
                pupilRepo.findById(grade.getPupilId()).get().getDepartment())) {
            if (!gradeRepo.existsBySubjectAndPupilAndGradeTypeLike(subRepo.findById(grade.getSubjectId()).get(),
                    pupilRepo.findById(grade.getPupilId()).get(),EGradeType.semester_finals_grade) &&
            !gradeRepo.existsBySubjectAndPupilAndGradeTypeLike((subRepo.findById(grade.getSubjectId()).get())
                    ,pupilRepo.findById(grade.getPupilId()).get(),EGradeType.year_finals_grade)){
                return true;
            }
        }
        return false;
    }



    @Override
    public Boolean checkforSchedule() {
        // TODO Auto-generated method stub
        return null;
    }

}
