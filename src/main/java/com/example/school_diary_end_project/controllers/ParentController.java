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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/parents")
public class ParentController {

    @Autowired
    private ParentRepository parentRepo;

    @Autowired
    private PupilRepository pupilRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping
    public ResponseEntity<?> getDb(){
        return new ResponseEntity<List<ParentEntity>>((List<ParentEntity>)parentRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addParent(@RequestBody ParentEntity parent){

        List<EUserRole> list = new ArrayList<>();
        list.add(EUserRole.ROLE_PARENT);
        parent.setRoles(list);
        if (parent.getPassword() != null){
            parent.setPassword(passwordEncoder.encode(parent.getPassword()));
        }


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

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParent(@PathVariable Integer id){
        if (parentRepo.findById(id).isPresent()) {
            ParentEntity temp = parentRepo.findById(id).get();
            String exit = String.format("Parent %s %s has been deleted",temp.getName(), temp.getSurname());
            //If a parent is deleted, all pupil fields that had this parent are set to null
            if (temp.getChildren().size()>0){
                for (PupilEntity pupil: pupilRepo.findAllByParent(temp)) {
                    pupil.setParent(null);

                }
            }

            parentRepo.deleteById(id);
            return new ResponseEntity<ParentEntity>(temp, HttpStatus.OK);

        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Parent not found"), HttpStatus.NOT_FOUND);
    }

    @Secured({"ROLE_TEACHER", "ROLE_ADMINISTRATOR", "ROLE_PARENT"})
    @RequestMapping("/{id}")
    public ResponseEntity<?> findById (@PathVariable Integer id){
        if (parentRepo.findById(id).isPresent()) {
            return new ResponseEntity<ParentEntity>(parentRepo.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Parent not found"), HttpStatus.NOT_FOUND);
    }


//    TODO create removeChild endpoint
    @RequestMapping( value="/{id]/child/{idpupil}", method = RequestMethod.PUT)
    public ResponseEntity<?> addChild (@PathVariable Integer id, @PathVariable Integer idpupil){
        if (pupilRepo.findById(idpupil).isPresent() && parentRepo.findById(id).isPresent()){
            pupilRepo.findById(idpupil).get().setParent(parentRepo.findById(id).get());
            pupilRepo.save(pupilRepo.findById(idpupil).get());
            return new ResponseEntity<ParentEntity>(parentRepo.findById(id).get(), HttpStatus.OK);

        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Parent or Pupil not found"), HttpStatus.NOT_FOUND);
    }
}
