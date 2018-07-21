package com.example.school_diary_end_project.entities;

import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Entity
@JsonIgnoreProperties({"Handler", "hibernateLazyInitializer"})
public abstract class UserEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Column(unique = true, nullable = false)
    private String username;

    @NotNull
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @NotNull
    @Column(unique = true, nullable = false)
    private String jmbg;

    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String surname;

    @NotNull
    @Past
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
    private LocalDate birthdate;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<EUserRole> roles;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }



    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getJmbg() {
        return jmbg;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public List<EUserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<EUserRole> roles) {
        this.roles = roles;
    }

    public UserEntity() {
    }
}
