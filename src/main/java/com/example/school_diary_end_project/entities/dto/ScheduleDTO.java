package com.example.school_diary_end_project.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class ScheduleDTO {

    @Null
    private String description;

    @NotNull(message = "Subject needed")
    private Integer subjectid;

    @NotNull(message = "Teacher needed")
    private Integer teacherid;

    @NotNull(message = "Department needed")
    private Integer departmentid;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Integer subjectid) {
        this.subjectid = subjectid;
    }

    public Integer getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(Integer teacherid) {
        this.teacherid = teacherid;
    }

    public Integer getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(Integer departmentid) {
        this.departmentid = departmentid;
    }

    public ScheduleDTO() {
        super();
        // TODO Auto-generated constructor stub
    }
}
