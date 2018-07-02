package com.example.school_diary_end_project.entities;


import com.example.school_diary_end_project.entities.enums.EGradeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties({"Handler", "hibernateLazyInitializer"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class, property = "id"
)
public class GradeEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String comment;

    @Column
    private Integer gradeValue;

    @Column
    private EGradeType gradeType;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
    private LocalDate dateOfGrading;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject")
    //JsonManagedReference
    private SubjectEntity subject;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "pupil")
    //@JsonManagedReference
    private PupilEntity pupil;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher")
    //@JsonManagedReference
    private TeacherEntity teacher;

    public LocalDate getDateOfGrading() {
        return dateOfGrading;
    }

    public void setDateOfGrading(LocalDate dateOfGrading) {
        this.dateOfGrading = dateOfGrading;
    }

    public TeacherEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherEntity teacher) {
        this.teacher = teacher;
    }

    public PupilEntity getPupil() {
        return pupil;
    }

    public void setPupil(PupilEntity pupil) {
        this.pupil = pupil;
    }

    public SubjectEntity getSubject() {
        return subject;
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(Integer gradeValue) {
        this.gradeValue = gradeValue;
    }

    public EGradeType getGradeType() {
        return gradeType;
    }

    public void setGradeType(EGradeType gradeType) {
        this.gradeType = gradeType;
    }

    public GradeEntity() {
    }
}
