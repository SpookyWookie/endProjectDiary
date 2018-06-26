package com.example.school_diary_end_project.entities;

import com.example.school_diary_end_project.entities.enums.ETeacherType;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class TeacherEntity extends UserEntity{

    private ArrayList<String> qualifiedForSubjects = new ArrayList<>();

    private ETeacherType teacherType;


    public void setQualifiedForSubjects(ArrayList<String> qualifiedForSubjects) {
        this.qualifiedForSubjects = qualifiedForSubjects;
    }

    public void setTeacherType(ETeacherType teacherType) {
        this.teacherType = teacherType;
    }

    public ArrayList<String> getQualifiedForSubjects() {
        return qualifiedForSubjects;
    }

    public ETeacherType getTeacherType() {
        return teacherType;
    }

    public TeacherEntity() {
    }
}
