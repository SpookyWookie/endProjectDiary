package com.example.school_diary_end_project.controllers;

import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.DepartmentEntity;
import com.example.school_diary_end_project.entities.PupilEntity;
import com.example.school_diary_end_project.entities.dto.PupilDto;
import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.example.school_diary_end_project.repositories.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;



@RestController
@RequestMapping("/pupils")
public class PupilController {

    @Autowired
    private PupilRepository pupilRepo;

    @Autowired
    private SubjectRepository subRepo;

    @Autowired
    private ParentRepository parentRepo;

    @Autowired
    private DepartmentRepository depoRepo;

    @Autowired
    private GradeRepository gradeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    private String createErrorMessage(BindingResult result) {
        return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
    }

    @RequestMapping
    public ResponseEntity<?> getDb() {
        return new ResponseEntity<List<PupilEntity>>((List<PupilEntity>) pupilRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addPupil(@Valid @RequestBody PupilDto newPupil, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }

        PupilEntity temp = new PupilEntity();


            temp.setBirthdate(newPupil.getBirthdate());



        if(userRepository.existsByEmail(newPupil.getEmail())){
            return new ResponseEntity<>(new RESTError(15, "Email already in use" ), HttpStatus.BAD_REQUEST);
        }
            temp.setEmail(newPupil.getEmail());


            if(userRepository.existsByJmbg(newPupil.getJmbg())){
                return new ResponseEntity<>(new RESTError(15, "JMBG already in use" ), HttpStatus.BAD_REQUEST);
            }

            temp.setJmbg(newPupil.getJmbg());



            temp.setName(newPupil.getName());



            temp.setSurname(newPupil.getSurname());



            if (userRepository.existsByUsername(newPupil.getUsername())){
                return new ResponseEntity<>(new RESTError(15, "Username already exists" ), HttpStatus.BAD_REQUEST);
            }
            temp.setUsername(newPupil.getUsername());




            temp.setPassword(passwordEncoder.encode(newPupil.getPassword()));




        List<EUserRole> list = new ArrayList<>();
        list.add(EUserRole.ROLE_PUPIL);

        temp.setRoles(list);
        if (newPupil.getPassword() != null){
            newPupil.setPassword(passwordEncoder.encode(newPupil.getPassword()));

        }
        return new ResponseEntity<PupilEntity>(pupilRepo.save(temp), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> editPupil(@Valid @PathVariable Integer id, @RequestBody PupilDto pupilon, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }


        if (pupilRepo.findById(id).isPresent()) {
            PupilEntity temp = pupilRepo.findById(id).get();

            if (!pupilon.getBirthdate().equals(null)) {
                temp.setBirthdate(pupilon.getBirthdate());
            }

            if (!pupilon.getEmail().equals(null)) {
                temp.setEmail(pupilon.getEmail());
            }

            if (!pupilon.getJmbg().equals(null)) {
                temp.setJmbg(pupilon.getJmbg());
            }

            if (!pupilon.getName().equals(null)) {
                temp.setName(pupilon.getName());
            }

            if (!pupilon.getSurname().equals(null)) {
                temp.setSurname(pupilon.getSurname());
            }

            if (!pupilon.getUsername().equals(null)) {
                temp.setUsername(pupilon.getUsername());
            }
            return new ResponseEntity<PupilEntity>(pupilRepo.save(temp), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Pupil not found"), HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePupil(@PathVariable Integer id) {
        if (pupilRepo.findById(id).isPresent()) {

            PupilEntity temp = pupilRepo.findById(id).get();
//            String exit = String.format("Pupil has been deleted",temp.getName(), temp.getSurname());
//            Deletes a parent if a parent has only one child

            if (temp.getParent() !=null){
                if (pupilRepo.findAllByParent(temp.getParent()).size() == 1){
                    parentRepo.delete(temp.getParent());
                }

                if (pupilRepo.findAllByParent(temp.getParent()).size() > 1){
                    parentRepo.findById(temp.getDepartment().getId()).get().getChildren().remove(temp);
                    parentRepo.save(temp.getParent());

                }
            }

            if (temp.getDepartment() != null){
                depoRepo.findById(temp.getDepartment().getId()).get().getPupils().remove(temp);
                depoRepo.save(temp.getDepartment());

            }

            if (temp.getGrades().size() > 0){
                gradeRepo.deleteAllByPupil(temp);
            }






            pupilRepo.deleteById(id);
            if (!pupilRepo.findById(id).isPresent()) {
                return new ResponseEntity<RESTError>(new RESTError(10, "Pupil deleted"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Pupil not found"), HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/{id}/addparent/{idp}", method = RequestMethod.PUT)
    public ResponseEntity<?> addParent(@PathVariable Integer id, @PathVariable Integer idp) {
        if (pupilRepo.findById(id).isPresent() && parentRepo.findById(idp).isPresent()) {
            pupilRepo.findById(id).get().setParent(parentRepo.findById(idp).get());
            return new ResponseEntity<PupilEntity>(pupilRepo.save(pupilRepo.findById(id).get()), HttpStatus.OK);

        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Pupil or Parent not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{id}/removeParent", method = RequestMethod.PUT)
    public ResponseEntity<?> removeParent(@PathVariable Integer id) {
        if (pupilRepo.findById(id).isPresent()) {
            pupilRepo.findById(id).get().setParent(null);
            return new ResponseEntity<PupilEntity>(pupilRepo.save(pupilRepo.findById(id).get()), HttpStatus.OK);

        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Pupil not found"), HttpStatus.NOT_FOUND);
    }

//    @Secured({"ROLE_TEACHER", "ROLE_ADMINISTRATOR", "ROLE_PARENT", "ROLE_PUPIL"})
    @RequestMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id){
        if (pupilRepo.findById(id).isPresent()){
            return new ResponseEntity<PupilEntity>(pupilRepo.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Pupil not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{id}/setDepartment/{idd}", method = RequestMethod.PUT)
    public ResponseEntity<?> setDepartment(@PathVariable Integer id, @PathVariable Integer idd) {
        if (pupilRepo.findById(id).isPresent() && depoRepo.findById(idd).isPresent()) {
            pupilRepo.findById(id).get().setDepartment(depoRepo.findById(idd).get());
            return new ResponseEntity<PupilEntity>(pupilRepo.save(pupilRepo.findById(id).get()), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Pupil or Department not found"), HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/{id}/removeDepartment", method = RequestMethod.PUT)
    public ResponseEntity<?> removeDepartment(@PathVariable Integer id) {
        if (pupilRepo.findById(id).isPresent()) {
            pupilRepo.findById(id).get().setDepartment(null);
            return new ResponseEntity<PupilEntity>(pupilRepo.save(pupilRepo.findById(id).get()), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Pupil not found"), HttpStatus.NOT_FOUND);
    }


}
