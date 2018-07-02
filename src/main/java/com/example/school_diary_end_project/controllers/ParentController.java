package com.example.school_diary_end_project.controllers;

import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.ParentEntity;
import com.example.school_diary_end_project.entities.PupilEntity;
import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.example.school_diary_end_project.repositories.ParentRepository;
import com.example.school_diary_end_project.repositories.PupilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parents")
public class ParentController {

    @Autowired
    private ParentRepository parentRepo;

    @Autowired
    private PupilRepository pupilRepo;

    @RequestMapping
    public ResponseEntity<?> getDb(){
        return new ResponseEntity<List<ParentEntity>>((List<ParentEntity>)parentRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addParent(@RequestBody ParentEntity parent){
        parent.setRole(EUserRole.ROLE_PARENT);

        return new ResponseEntity<ParentEntity>(parentRepo.save(parent), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> editParent(@RequestBody ParentEntity parent, @PathVariable Integer id) {
        if (parentRepo.findById(id).isPresent()) {
            ParentEntity temp = parentRepo.findById(id).get();

            if (!parent.getBirthdate().equals(null)) {
                temp.setBirthdate(parent.getBirthdate());
            }

            if (!parent.getEmail().equals(null)) {
                temp.setEmail(parent.getEmail());
            }

            if (!parent.getJmbg().equals(null)) {
                temp.setJmbg(parent.getJmbg());
            }

            if (!parent.getName().equals(null)) {
                temp.setName(parent.getName());
            }


            if (!parent.getSurname().equals(null)) {
                temp.setSurname(parent.getSurname());;
            }

            if (!parent.getUsername().equals(null)) {
                temp.setUsername(parent.getUsername());
            }


            return new ResponseEntity<ParentEntity>(parentRepo.save(temp), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Parent not found"), HttpStatus.NOT_FOUND);

    }
//TODO find out of there is annotation that clears all referenced fields, and is there an annotation that sets default value after deletion of a certain entity
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParent(@PathVariable Integer id){
        if (parentRepo.findById(id).isPresent()) {
            ParentEntity temp = parentRepo.findById(id).get();
            String exit = String.format("Parent %s %s has been deleted",temp.getName(), temp.getSurname());
            //If a parent is deleted, all fields that had this parent are set to null
            for (PupilEntity pupil: pupilRepo.findAllByParent(temp)) {
                pupil.setParent(null);

            }
            parentRepo.deleteById(id);
            return new ResponseEntity<ParentEntity>(temp, HttpStatus.OK);

        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Parent not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<?> findById (@PathVariable Integer id){
        if (parentRepo.findById(id).isPresent()) {
            return new ResponseEntity<ParentEntity>(parentRepo.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Parent not found"), HttpStatus.NOT_FOUND);
    }
}
