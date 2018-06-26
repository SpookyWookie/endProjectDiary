package com.example.school_diary_end_project.repositories;

import com.example.school_diary_end_project.entities.TeacherEntity;
import org.springframework.data.repository.CrudRepository;

public interface TeacherRepository extends CrudRepository<TeacherEntity, Integer> {
}
