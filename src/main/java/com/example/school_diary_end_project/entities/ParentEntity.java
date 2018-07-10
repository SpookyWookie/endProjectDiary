package com.example.school_diary_end_project.entities;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class, property = "id"
)
public class ParentEntity extends UserEntity {

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "parent")
    private List<PupilEntity> children;

    public List<PupilEntity> getChildren() {
        return children;
    }

    public void setChildren(List<PupilEntity> children) {
        this.children = children;


    }

    public ParentEntity() {
    }
}
