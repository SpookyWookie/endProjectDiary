package com.example.school_diary_end_project.controllers;


import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.AdministratorEntity;
import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.example.school_diary_end_project.repositories.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {

    @Autowired
    private AdministratorRepository adminRepo;

    @RequestMapping
    public ResponseEntity<?> getDb(){
        return new ResponseEntity<List<AdministratorEntity>>((List<AdministratorEntity>)adminRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addAdministrator(@RequestBody AdministratorEntity admin){
        admin.setRole(EUserRole.ROLE_ADMINISTRATOR);
        return new ResponseEntity<AdministratorEntity>(adminRepo.save(admin), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> editAdministrator(@RequestBody AdministratorEntity admin, @PathVariable Integer id) {
        if (adminRepo.findById(id).isPresent()) {
            AdministratorEntity temp = adminRepo.findById(id).get();

            if (!admin.getBirthdate().equals(null)) {

                temp.setBirthdate(admin.getBirthdate());
            }

            if (!admin.getEmail().equals(null)) {
                temp.setEmail(admin.getEmail());
            }

            if (!admin.getJmbg().equals(null)) {
                temp.setJmbg(admin.getJmbg());
            }

            if (!admin.getName().equals(null)) {
                temp.setName(admin.getName());
            }

            if (!admin.getSurname().equals(null)) {
                temp.setSurname(admin.getSurname());
            }

            if (!admin.getUsername().equals(null)) {
                temp.setUsername(admin.getUsername());
            }

            return new ResponseEntity<AdministratorEntity>(adminRepo.save(temp), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Administrator not found"), HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParent(@PathVariable Integer id){
        if (adminRepo.findById(id).isPresent()) {
            AdministratorEntity temp = adminRepo.findById(id).get();

            adminRepo.deleteById(id);
            return new ResponseEntity<AdministratorEntity>(temp, HttpStatus.OK);

        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Administrator not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<?> findById (@PathVariable Integer id){
        if (adminRepo.findById(id).isPresent()) {
            return new ResponseEntity<AdministratorEntity>(adminRepo.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Administrator not found"), HttpStatus.NOT_FOUND);
    }
}
