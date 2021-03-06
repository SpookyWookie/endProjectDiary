package com.example.school_diary_end_project.entities;

import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Handler;

@Entity
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class, property = "id"
)
public class PupilEntity extends UserEntity {

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private ParentEntity parent;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "pupil")
    //@JsonBackReference
    private List<GradeEntity> grades;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "department")
    private DepartmentEntity department;

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    public List<GradeEntity> getGrades() {
        return grades;
    }

    public void setGrades(List<GradeEntity> grades) {
        this.grades = grades;
    }

    public void setParent(ParentEntity parent) {
        this.parent = parent;
    }

    public ParentEntity getParent() {
        return parent;
    }

    public PupilEntity() {
    }

    public PupilEntity(PupilEntity pupil) {
        super(pupil.getId(), pupil.getUsername(), pupil.getPassword(), pupil.getJmbg(), pupil.getEmail(), pupil.getName(), pupil.getSurname(), pupil.getBirthdate(), pupil.getRoles());
        this.parent = pupil.getParent();
        this.grades = pupil.getGrades();
        this.department = pupil.getDepartment();
    }
}
