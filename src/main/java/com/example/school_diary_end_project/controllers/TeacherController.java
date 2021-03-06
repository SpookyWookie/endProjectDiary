package com.example.school_diary_end_project.controllers;


import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.TeacherEntity;
import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.example.school_diary_end_project.repositories.DepartmentRepository;
import com.example.school_diary_end_project.repositories.GradeRepository;
import com.example.school_diary_end_project.repositories.ScheduleRepository;
import com.example.school_diary_end_project.repositories.TeacherRepository;
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
@RequestMapping("/teachers")
public class TeacherController {



    @Autowired
    private TeacherRepository teachRepo;


    @Autowired
    private DepartmentRepository depoRepo;

    @Autowired
    private GradeRepository gradeRepo;

    @Autowired
    private ScheduleRepository scheduleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String createErrorMessage(BindingResult result) {
        return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
    }

    @RequestMapping
    public ResponseEntity<?> getDb() {
        return new ResponseEntity<List<TeacherEntity>>((List<TeacherEntity>) teachRepo.findAll(), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addTeacher(@Valid @RequestBody TeacherEntity teacher, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }

        List<EUserRole> list = new ArrayList<>();
        list.add(EUserRole.ROLE_TEACHER);
        teacher.setRoles(list);

        if (teacher.getPassword() != null){
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));

        }
        return new ResponseEntity<TeacherEntity>(teachRepo.save(teacher), HttpStatus.OK);
    }


//    @Secured({"ROLE_TEACHER", "ROLE_ADMINISTRATOR"})
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> editTeacher(@Valid @RequestBody TeacherEntity teacher, @PathVariable Integer id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }
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

            if ( teacher.getTeacherType() != null) {
                temp.setTeacherType(teacher.getTeacherType());
            }

            if (!teacher.getQualifiedForSubjects().equals(null)) {
                temp.setQualifiedForSubjects(teacher.getQualifiedForSubjects());
            }

            return new ResponseEntity<TeacherEntity>(teachRepo.save(temp), HttpStatus.OK);

        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);
    }

//    @Secured({"ROLE_TEACHER", "ROLE_ADMINISTRATOR"})
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Integer id) {
        if (teachRepo.findById(id).isPresent()) {
            TeacherEntity temp = teachRepo.findById(id).get();
            String exit = String.format("Teacher %s %s has been deleted", temp.getName(), temp.getSurname());
            if (temp.getGrades().size() > 0) {
                return new ResponseEntity<RESTError>(new RESTError(1, "Teacher cannot be deleted, please remove his connections to grades first"), HttpStatus.BAD_REQUEST);


            }

            if (temp.getSchedule().size() > 0) {
                return new ResponseEntity<RESTError>(new RESTError(1, "Teacher cannot be deleted, please remove his connections to schedule first"), HttpStatus.BAD_REQUEST);
            }

            if (temp.getHeadOfDepartment() != null) {
                return new ResponseEntity<RESTError>(new RESTError(1, "Teacher cannot be deleted, please remove his connections as head of department first"), HttpStatus.BAD_REQUEST);
            }


            teachRepo.deleteById(id);

            return new ResponseEntity<String>(exit, HttpStatus.OK);
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);


    }

//    @Secured({"ROLE_TEACHER", "ROLE_ADMINISTRATOR"})
    @RequestMapping("/{id}")
    private ResponseEntity<?> findById(@PathVariable Integer id){
        if (teachRepo.existsById(id)){
            return new ResponseEntity<TeacherEntity>(teachRepo.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);
    }



    //	dodeljuje razrednog staresinu odeljenju
//    @Secured({"ROLE_TEACHER", "ROLE_ADMINISTRATOR"})
    @RequestMapping(value = "/{id}/department/{idd}", method = RequestMethod.PUT)
    public ResponseEntity<?> setHeadOfDepartment(@PathVariable Integer id, @PathVariable Integer idd){
        if (teachRepo.findById(id).isPresent() && depoRepo.findById(idd).isPresent()) {
            teachRepo.findById(id).get().setHeadOfDepartment(depoRepo.findById(idd).get());

            return new ResponseEntity<TeacherEntity>(teachRepo.save(teachRepo.findById(id).get()), HttpStatus.OK);
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Teacher or Department not found"), HttpStatus.NOT_FOUND);
    }


    //	oduzima staresinstvo razrednom
//    @Secured({"ROLE_TEACHER", "ROLE_ADMINISTRATOR"})
    @RequestMapping(value = "/{id}/removeDepartment", method = RequestMethod.PUT)
    public ResponseEntity<?> removeHeadOfDepartment(@PathVariable Integer id){
        if (teachRepo.findById(id).isPresent() && teachRepo.findById(id).get().getHeadOfDepartment() != null) {
//            depoRepo.findById(idd).get().setHeadTeacher(null);
//            depoRepo.save(depoRepo.findById(id).get());
            teachRepo.findById(id).get().setHeadOfDepartment(null);
            teachRepo.save(teachRepo.findById(id).get());

            return new ResponseEntity<TeacherEntity>(teachRepo.save(teachRepo.findById(id).get()), HttpStatus.OK);
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Teacher or Department not assigned found"), HttpStatus.NOT_FOUND);
    }




// Removes all assigned references
//    @Secured({"ROLE_TEACHER", "ROLE_ADMINISTRATOR"})
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/unAssign")
        public ResponseEntity<?> removeAllReferences(@PathVariable Integer id){
            if(teachRepo.findById(id).isPresent()){
                TeacherEntity temp = teachRepo.findById(id).get();
                if (temp.getHeadOfDepartment() != null){
                    temp.setHeadOfDepartment(null);
                }

                if (temp.getGrades().size() > 0){
                    gradeRepo.deleteAllByTeacher(temp);
                }

                if (temp.getSchedule().size() > 0){
                    scheduleRepo.deleteAllByTeacher(temp);
                }

                return new ResponseEntity<TeacherEntity>(teachRepo.save(teachRepo.findById(id).get()), HttpStatus.OK);
            }
            return new ResponseEntity<RESTError>(new RESTError(1, "Teacher not found"), HttpStatus.NOT_FOUND);

        }

}
