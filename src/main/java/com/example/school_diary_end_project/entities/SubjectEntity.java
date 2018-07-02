package com.example.school_diary_end_project.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIgnoreProperties({"Handler", "hibernateLazyInitializer"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class, property = "id"
)
public class SubjectEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Integer lessonsInAWeek;

    @Column
    private Integer semesterlessonsfund;

    @Column
    private Integer year;

    // veza predmeta, profesora i odeljenja koja pohadaju predmet
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "subject")
    @JsonIgnoreProperties("subject")
    private List<ScheduleEntity> schedule;

    // veza predmeta i ocena iz njega
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "subject")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLessonsInAWeek() {
        return lessonsInAWeek;
    }

    public void setLessonsInAWeek(Integer lessonsInAWeek) {
        this.lessonsInAWeek = lessonsInAWeek;
    }

    public Integer getSemesterlessonsfund() {
        return semesterlessonsfund;
    }

    public void setSemesterlessonsfund(Integer semesterlessonsfund) {
        this.semesterlessonsfund = semesterlessonsfund;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public SubjectEntity() {
    }

}
