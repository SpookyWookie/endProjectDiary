package com.example.school_diary_end_project.repositories;

import com.example.school_diary_end_project.entities.ParentEntity;
import com.example.school_diary_end_project.entities.PupilEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PupilRepository extends CrudRepository<PupilEntity, Integer> {

    public List<PupilEntity> findAllByParent(ParentEntity parent);
}
