package com.example.school_diary_end_project.repositories;

import com.example.school_diary_end_project.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
}
