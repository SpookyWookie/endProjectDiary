package com.example.school_diary_end_project.repositories;

import com.example.school_diary_end_project.entities.DepartmentEntity;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Integer> {
}
