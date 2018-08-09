package com.example.school_diary_end_project.controllers;

import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.ParentEntity;
import com.example.school_diary_end_project.entities.PupilEntity;
import com.example.school_diary_end_project.entities.dto.ParentDto;
import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.example.school_diary_end_project.repositories.ParentRepository;
import com.example.school_diary_end_project.repositories.PupilRepository;
import com.example.school_diary_end_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/parents")
public class ParentController {

    @Autowired
    private ParentRepository parentRepo;

    @Autowired
    private PupilRepository pupilRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String createErrorMessage(BindingResult result) {
        return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
    }


    @RequestMapping
    public ResponseEntity<?> getDb(){
        return new ResponseEntity<List<ParentEntity>>((List<ParentEntity>)parentRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addParent(@Valid @RequestBody ParentDto parent, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }

        ParentEntity newParent = new ParentEntity();

        newParent.setBirthdate(parent.getBirthdate());

        if(userRepository.existsByEmail(parent.getEmail())){
            return new ResponseEntity<>(new RESTError(15, "Email already in use" ), HttpStatus.BAD_REQUEST);
        }
        newParent.setEmail(parent.getEmail());


        if(userRepository.existsByJmbg(parent.getJmbg())){
            return new ResponseEntity<>(new RESTError(15, "JMBG already in use" ), HttpStatus.BAD_REQUEST);
        }
        newParent.setJmbg(parent.getJmbg());

        newParent.setName(parent.getName());

        newParent.setSurname(parent.getSurname());

        if (userRepository.existsByUsername(parent.getUsername())){
            return new ResponseEntity<>(new RESTError(15, "Username already exists" ), HttpStatus.BAD_REQUEST);
        }
        newParent.setUsername(parent.getUsername());

//        newParent.setPassword(passwordEncoder.encode(parent.getPassword()));

        List<EUserRole> list = new ArrayList<>();
        list.add(EUserRole.ROLE_PARENT);

        newParent.setRoles(list);
        if (parent.getPassword() != null){
            newParent.setPassword(passwordEncoder.encode(parent.getPassword()));

        }

        if (parent.getPassword() != null){
            newParent.setPassword(passwordEncoder.encode(parent.getPassword()));
        }


        return new ResponseEntity<ParentEntity>(parentRepo.save(newParent), HttpStatus.OK);
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

//    @Secured({"ROLE_TEACHER", "ROLE_ADMINISTRATOR", "ROLE_PARENT"})
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
