package com.example.school_diary_end_project.repositories;

import com.example.school_diary_end_project.entities.GradeEntity;
import com.example.school_diary_end_project.entities.PupilEntity;
import com.example.school_diary_end_project.entities.SubjectEntity;
import com.example.school_diary_end_project.entities.TeacherEntity;
import com.example.school_diary_end_project.entities.enums.EGradeType;
import org.springframework.data.repository.CrudRepository;

public interface GradeRepository extends CrudRepository <GradeEntity, Integer> {

    public Boolean existsBySubjectAndPupilAndGradeTypeLike(SubjectEntity subject, PupilEntity pupil, EGradeType gradeType);

    public void deleteAllByPupil(PupilEntity pupil);

    public void deleteAllByTeacher(TeacherEntity teacher);

    public void deleteAllBySubject(SubjectEntity subject);

}
