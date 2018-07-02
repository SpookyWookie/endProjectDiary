package com.example.school_diary_end_project.repositories;

import com.example.school_diary_end_project.entities.DepartmentEntity;
import com.example.school_diary_end_project.entities.ScheduleEntity;
import com.example.school_diary_end_project.entities.SubjectEntity;
import com.example.school_diary_end_project.entities.TeacherEntity;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleRepository extends CrudRepository<ScheduleEntity, Integer> {

    public Boolean existsByTeacherAndSubjectAndDepartment(TeacherEntity teacher, SubjectEntity subject, DepartmentEntity department);
}
