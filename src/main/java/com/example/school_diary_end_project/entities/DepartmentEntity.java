package com.example.school_diary_end_project.entities;

import com.example.school_diary_end_project.entities.enums.EDepartmentEnum;
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
public class DepartmentEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private EDepartmentEnum enumeration;

    @Column
    private Integer year;


    //veza odeljenja i razrednog
    @OneToOne(mappedBy = "headOfDepartment")
    private TeacherEntity headTeacher;

    public TeacherEntity getHeadTeacher() {
        return headTeacher;
    }

    //veza odeljenja i liste profesora, odeljenja i predmeta
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "department")
    @JsonIgnoreProperties("department")
    private List<ScheduleEntity> schedule;

    //veza odeljenja i ucenika koji ga pohadjaju
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "department")
    private List<PupilEntity> pupils;

    public List<PupilEntity> getPupils() {
        return pupils;
    }

    public void setPupils(List<PupilEntity> pupils) {
        this.pupils = pupils;
    }

    public List<ScheduleEntity> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleEntity> schedule) {
        this.schedule = schedule;
    }

    public void setHeadTeacher(TeacherEntity headTeacher) {
        this.headTeacher = headTeacher;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EDepartmentEnum getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(EDepartmentEnum enumeration) {
        this.enumeration = enumeration;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public DepartmentEntity() {
    }
}
