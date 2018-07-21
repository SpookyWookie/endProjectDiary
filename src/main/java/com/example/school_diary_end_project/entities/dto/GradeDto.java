package com.example.school_diary_end_project.entities.dto;

import com.example.school_diary_end_project.entities.enums.EGradeType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

public class GradeDto {

    @NotNull
    @Pattern(regexp = "^[12345]{1}$", message = "Grade not in required range(1-5)")
    private Integer value;

    @NotNull(message = "Professor needed")
    private Integer professorId;

    @NotNull(message = "Subject needed")
    private Integer subjectId;

    @NotNull(message = "Pupil needed")
    private Integer pupilId;


    @Null
    private String comment;


    @NotNull(message = "Grade type needed")
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
