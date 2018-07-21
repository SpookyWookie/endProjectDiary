package com.example.school_diary_end_project.entities.dto;

import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class PupilDto {



    @Size(min = 3, max = 15, message = "Username size must be between {min} and {max} characters long")
    @NotNull(message = "Username must be provided")
    private String username;



    @Size(min = 3, max = 15, message = "Password size must be between {min} and {max} characters long")
    @NotNull(message = "Password must be provided")
    private String password;


    @Size(min = 13, max = 13, message = "JMBG size must be {max} characters long")
    @NotNull(message = "JMBG must be provided")
    private String jmbg;


    @NotNull(message = "E-mail must be provided")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email is not valid.")
    private String email;


    @Size(min = 2, max = 15, message = "Name size must be between {min} and {max} characters long")
    @NotNull(message = "Name must be provided")
    private String name;

    @Size(min = 2, max = 15, message = "Surname size must be between {min} and {max} characters long")
    @NotNull(message = "Surname must be provided")
    private String surname;

    @NotNull(message = "Birth date must be provided")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
    private LocalDate birthdate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public PupilDto() {
    }
}
