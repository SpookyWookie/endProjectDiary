package com.example.school_diary_end_project.services;

import com.example.school_diary_end_project.entities.GradeEntity;

public interface EmailService {

    void sendTemplateMessage (GradeEntity grade) throws Exception;
}
