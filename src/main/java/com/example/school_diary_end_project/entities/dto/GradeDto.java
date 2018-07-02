package com.example.school_diary_end_project.entities.dto;

import com.example.school_diary_end_project.entities.enums.EGradeType;

public class GradeDto {

    private Integer value;

    private Integer professorId;

    private Integer subjectId;

    private Integer pupilId;


    private String comment;


    private EGradeType gradeType;



    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public EGradeType getGradeType() {
        return gradeType;
    }

    public void setGradeType(EGradeType gradeType) {
        this.gradeType = gradeType;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }


    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getPupilId() {
        return pupilId;
    }

    public void setPupilId(Integer pupilId) {
        this.pupilId = pupilId;
    }

    public GradeDto() {
        super();
        // TODO Auto-generated constructor stub
    }
}
