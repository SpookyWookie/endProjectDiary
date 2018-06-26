package com.example.school_diary_end_project.controllers;

import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.PupilEntity;
import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.example.school_diary_end_project.repositories.ParentRepository;
import com.example.school_diary_end_project.repositories.PupilRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
@RequestMapping("/pupils")
public class PupilController {

    @Autowired
    private PupilRepository pupilRepo;

/*    @Autowired
    private SubjectRepository subRepo;*/

    @Autowired
    private ParentRepository parentRepo;

  /*  @Autowired
    private DepartmentRepository depoRepo;*/

    @RequestMapping
    public ResponseEntity<?> getDb() {
        return new ResponseEntity<List<PupilEntity>>((List<PupilEntity>) pupilRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addPupil(@RequestBody PupilEntity newPupil) {
        newPupil.setRole(EUserRole.ROLE_PUPIL);
        return new ResponseEntity<PupilEntity>(pupilRepo.save(newPupil), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> editPupil(@PathVariable Integer id, @RequestBody PupilEntity pupilon) {
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

  /*  // Opciona metoda za testiranje
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/subject/{ids}")
    public ResponseEntity<?> editSubject(@PathVariable Integer id, @PathVariable Integer ids) {
        SubjectEntity temp = subRepo.findById(ids).get();
        pupilRepo.findById(id).get().addSubjects(temp);
        return new ResponseEntity<PupilEntity>(pupilRepo.save(pupilRepo.findById(id).get()), HttpStatus.OK);

    }*/

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePupil(@PathVariable Integer id) {
        if (pupilRepo.findById(id).isPresent()) {

            PupilEntity temp = pupilRepo.findById(id).get();
            String exit = String.format("Pupil %s %s has been deleted",temp.getName(), temp.getSurname());
//            Deletes a parent if a parent has only one child
            if (pupilRepo.findAllByParent(temp.getParent()).size() == 1){
                parentRepo.delete(temp.getParent());
            }



            pupilRepo.deleteById(id);
            if (!pupilRepo.findById(id).isPresent()) {
                return new ResponseEntity<String>(exit, HttpStatus.OK);
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

 /*   @RequestMapping(value = "/{id}/setDepartment/{idd}", method = RequestMethod.PUT)
    public ResponseEntity<?> setDepartment(@PathVariable Integer id, @PathVariable Integer idd) {
        if (pupilRepo.findById(id).isPresent() && depoRepo.findById(idd).isPresent()) {
            pupilRepo.findById(id).get().setDepartment(depoRepo.findById(idd).get());
            if (!depoRepo.findById(idd).get().getSchoolYear().getSubjectsListened().isEmpty()) {
                Set<SubjectEntity> subjectsSet = new HashSet<>(depoRepo.findById(idd).get().getSchoolYear().getSubjectsListened());
                pupilRepo.findById(id).get().setSubjects(subjectsSet);
            }


            return new ResponseEntity<PupilEntity>(pupilRepo.save(pupilRepo.findById(id).get()), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Pupil or Department not found"), HttpStatus.NOT_FOUND);

    }*/

  /*  //azurira listu predmeta za ucenike(izmeniti)
    public ResponseEntity<?> updateSubjects(@PathVariable Integer id) {
        if (pupilRepo.findById(id).isPresent() && pupilRepo.findById(id).get().getDepartment().equals(null)) {
            //bezveze
        }
        if (pupilRepo.findById(id).isPresent()) {
            PupilEntity temp = pupilRepo.findById(id).get();
            if (temp.getDepartment().equals(null) && temp.getDepartment().getSchoolYear().equals(null)
                    && temp.getDepartment().getSchoolYear().getSubjectsListened().isEmpty()) {

                return new ResponseEntity<RESTError>(new RESTError(1, "No subjects found"), HttpStatus.NOT_FOUND);

            }
            temp.setSubjects((Set<SubjectEntity>) temp.getDepartment().getSchoolYear().getSubjectsListened());

            return new ResponseEntity<PupilEntity>(pupilRepo.save(pupilRepo.findById(id).get()), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Pupil or Subjects not found"), HttpStatus.NOT_FOUND);

    }*/
}
