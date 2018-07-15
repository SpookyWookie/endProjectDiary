package com.example.school_diary_end_project.services;

import com.example.school_diary_end_project.entities.UserEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    public String signin(String username, String password);

    public UserEntity whoami(HttpServletRequest req);

    public UserEntity search(String username);

    public void delete(String username);


}
