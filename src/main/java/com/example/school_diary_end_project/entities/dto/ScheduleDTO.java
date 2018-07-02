package com.example.school_diary_end_project.entities.dto;

public class ScheduleDTO {

    private String description;

    private Integer subjectid;

    private Integer teacherid;

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
