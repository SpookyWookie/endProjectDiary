package com.example.school_diary_end_project.repositories;

import com.example.school_diary_end_project.entities.DepartmentEntity;
import com.example.school_diary_end_project.entities.ParentEntity;
import com.example.school_diary_end_project.entities.PupilEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PupilRepository extends CrudRepository<PupilEntity, Integer> {

    public List<PupilEntity> findAllByParent(ParentEntity parent);

    public void deleteAllByDepartment(DepartmentEntity department);
//
//    @Query("SELECT a FROM User_entity a where lower(ue.username) like lower(concat('%', :username,'%'))")
//    List<PupilEntity> searchByUsername(String username);

    List<PupilEntity> findAllByUsernameContainingIgnoreCase(String username);
}
