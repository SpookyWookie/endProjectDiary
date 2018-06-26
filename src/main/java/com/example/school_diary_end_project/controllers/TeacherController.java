package com.example.school_diary_end_project.controllers;


import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.TeacherEntity;
import com.example.school_diary_end_project.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherRepository teachRepo;

/*    @Autowired
    private SubjectRepository subRepo;*/

//    @Autowired
//    private DepartmentRepository depoRepo;

    @RequestMapping
    public ResponseEntity<?> getDb(){
        return new ResponseEntity<List<TeacherEntity>>((List<TeacherEntity>)teachRepo.findAll(), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addTeacher(@RequestBody TeacherEntity teacher){
        return new ResponseEntity<TeacherEntity>(teachRepo.save(teacher), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> editTeacher(@RequestBody TeacherEntity teacher, @PathVariable Integer id){
        if (teachRepo.findById(id).isPresent()) {
            TeacherEntity temp = teachRepo.findById(id).get();

            if (!teacher.getBirthdate().equals(null)) {
                temp.setBirthdate(teacher.getBirthdate());
            }

            if (!teacher.getEmail().equals(null)) {
                temp.setEmail(teacher.getEmail());
            }

            if (!teacher.getJmbg().equals(null)) {
                temp.setJmbg(teacher.getJmbg());
            }

            if (!teacher.getName().equals(null)) {
                temp.setName(teacher.getName());
            }

            if (!teacher.getSurname().equals(null)) {
                temp.setSurname(teacher.getSurname());
            }

            if (!teacher.getUsername().equals(null)) {
                temp.setUsername(teacher.getUsername());
            }

            if (!teacher.getTeacherType().equals(null)) {
                temp.setTeacherType(teacher.getTeacherType());
            }

            if (!teacher.getQualifiedForSubjects().equals(null)){
                temp.setQualifiedForSubjects(teacher.getQualifiedForSubjects());
            }

            return new ResponseEntity<TeacherEntity>(teachRepo.save(temp), HttpStatus.OK);

        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Integer id){
        if (teachRepo.findById(id).isPresent()) {
            TeacherEntity temp = teachRepo.findById(id).get();
            String exit = String.format("Teacher %s %s has been deleted",temp.getName(), temp.getSurname());
            teachRepo.deleteById(id);

            return new ResponseEntity<String>(exit, HttpStatus.OK);
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);


    }

  /*  //	dodeljuje predmet profesoru
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/subject/{ids}")
    public ResponseEntity<?> assignSubject(@PathVariable Integer id, @PathVariable Integer ids){
        if (teachRepo.findById(id).isPresent() && subRepo.findById(ids).isPresent()) {

            Set<SubjectEntity> subjects = teachRepo.findById(id).get().getSubjects();
            subjects.add(subRepo.findById(ids).get());

            teachRepo.findById(id).get().setSubjects(subjects);

            return new ResponseEntity<TeacherEntity>(teachRepo.save(teachRepo.findById(id).get()), HttpStatus.OK);
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Teacher or Subject not found"), HttpStatus.NOT_FOUND);
    }

    //	dodeljuje razrednog staresinu odeljenju
    @RequestMapping(value = "/{id}/department/{idd}", method = RequestMethod.PUT)
    public ResponseEntity<?> setHeadOfDepartment(@PathVariable Integer id, @PathVariable Integer idd){
        if (teachRepo.findById(id).isPresent() && depoRepo.findById(idd).isPresent()) {
            teachRepo.findById(id).get().setHeadOfDepartment(depoRepo.findById(idd).get());

            return new ResponseEntity<TeacherEntity>(teachRepo.save(teachRepo.findById(id).get()), HttpStatus.OK);
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Teacher or Department not found"), HttpStatus.NOT_FOUND);
    }*/
}
