package com.example.school_diary_end_project.entities;

import com.example.school_diary_end_project.entities.enums.ETeacherType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"Handler", "hibernateLazyInitializer"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class, property = "id"
)
public class TeacherEntity extends UserEntity{

    private ArrayList<String> qualifiedForSubjects = new ArrayList<>();

    private ETeacherType teacherType;

    //veza razrednog i odeljenja
    @OneToOne
    @JoinColumn(name = "headOfDepartment")
    private DepartmentEntity headOfDepartment;

    //veza profesora prema predmetu i odeljenju uz kog je dodeljen
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "teacher")
    private List<ScheduleEntity> schedule;

    //lista profesora i ocena koje je dao
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "teacher")
    private List<GradeEntity> grades;

    public List<GradeEntity> getGrades() {
        return grades;
    }

    public void setGrades(List<GradeEntity> grades) {
        this.grades = grades;
    }

    public List<ScheduleEntity> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleEntity> schedule) {
        this.schedule = schedule;
    }

    public DepartmentEntity getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(DepartmentEntity headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

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
