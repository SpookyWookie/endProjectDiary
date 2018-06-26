package com.example.school_diary_end_project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.logging.Handler;

@Entity
@JsonIgnoreProperties({"Handler", "hibernateLazyInitializer"})
public class PupilEntity extends UserEntity {

    //@JsonManagedReference(value = "frontend")
    @JsonManagedReference
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private ParentEntity parent;

    public void setParent(ParentEntity parent) {
        this.parent = parent;
    }

    public ParentEntity getParent() {
        return parent;
    }

    public PupilEntity() {
    }
}
