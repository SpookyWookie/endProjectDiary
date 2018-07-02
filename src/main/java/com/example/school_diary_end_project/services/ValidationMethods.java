package com.example.school_diary_end_project.services;

import com.example.school_diary_end_project.entities.dto.GradeDto;

public interface ValidationMethods {

    public Boolean checkForGrade(GradeDto grade);

    public Boolean checkforSchedule();
}
