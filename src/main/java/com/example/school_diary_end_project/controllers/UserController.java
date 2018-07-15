package com.example.school_diary_end_project.controllers;

import com.example.school_diary_end_project.entities.UserEntity;
import com.example.school_diary_end_project.exception.CustomException;
import com.example.school_diary_end_project.repositories.UserRepository;
import com.example.school_diary_end_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

//TODO make create user endpoint.!

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @RequestMapping("/signin")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password){
        return userService.signin(username, password);
    }


    @RequestMapping("/me")
    public ResponseEntity getMe(HttpServletRequest req){
        return new ResponseEntity(userService.whoami(req), HttpStatus.OK);
    }


    @RequestMapping("/getAllUsers")
    public ResponseEntity getAll(){
        return new ResponseEntity(userRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping("/search")
    public ResponseEntity search(@RequestParam("username") String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(user, HttpStatus.OK);
    }
}
