package com.example.school_diary_end_project.entities.enums;

import org.springframework.security.core.GrantedAuthority;

public enum EUserRole implements GrantedAuthority  {
    ROLE_ADMINISTRATOR, ROLE_PUPIL, ROLE_TEACHER, ROLE_PARENT;

    public String getAuthority() {
        return name();
    }
}
