package com.example.school_diary_end_project.repositories;

import com.example.school_diary_end_project.entities.ScheduleEntity;
import com.example.school_diary_end_project.entities.SubjectEntity;
import org.springframework.data.repository.CrudRepository;

public interface SubjectRepository extends CrudRepository<SubjectEntity, Integer> {

    public SubjectEntity findBySchedule(ScheduleEntity schedule);
}
