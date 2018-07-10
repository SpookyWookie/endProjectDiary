package com.example.school_diary_end_project.controllers;

import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.DepartmentEntity;
import com.example.school_diary_end_project.repositories.DepartmentRepository;
import com.example.school_diary_end_project.repositories.PupilRepository;
import com.example.school_diary_end_project.repositories.ScheduleRepository;
import com.example.school_diary_end_project.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private PupilRepository pupilRepo;

    @Autowired
    private TeacherRepository teachRepo;

    @Autowired
    private ScheduleRepository scheduleRepo;



    @RequestMapping
    public ResponseEntity<?> getDb() {
        return new ResponseEntity<List<DepartmentEntity>>((List<DepartmentEntity>) departmentRepo.findAll(), HttpStatus.OK);
    }


//    TODO when creating department, enable only creation of enumerated departments in order (8 departments are able to be created)
//    TODO hipoteticki ofc
//    And disable ability to change department enum
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addDepartment(@RequestBody DepartmentEntity newDepartment) {

        return new ResponseEntity<DepartmentEntity>(departmentRepo.save(newDepartment), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> editDepartment(@RequestBody DepartmentEntity editDepartment, @PathVariable Integer id) {
        if (departmentRepo.findById(id).isPresent()) {
            DepartmentEntity temp = departmentRepo.findById(id).get();
            if (!editDepartment.getEnumeration().equals(null)) {
                temp.setEnumeration(editDepartment.getEnumeration());
            }

            if (!editDepartment.getYear().equals(null)) {
                temp.setYear(editDepartment.getYear());
            }



            return new ResponseEntity<DepartmentEntity>(departmentRepo.save(temp), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Department not found"), HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDepartment(@PathVariable Integer id) {
        if (departmentRepo.findById(id).isPresent()) {
            DepartmentEntity temp = departmentRepo.findById(id).get();
            if (temp.getPupils() != null && temp.getPupils().size()>0){
                return new ResponseEntity<RESTError>(new RESTError(2, "Department cannot be deleted if there are any pupils in it"), HttpStatus.BAD_REQUEST);
            }

            if (temp.getHeadTeacher() != null){
                return new ResponseEntity<RESTError>(new RESTError(3, "Department cannot be deleted if teacher is assigned"), HttpStatus.BAD_REQUEST);
            }

            if (temp.getSchedule() != null && temp.getSchedule().size()>0){
                return new ResponseEntity<RESTError>(new RESTError(4, "Department cannot be deleted if schedule refers to it more than once"), HttpStatus.BAD_REQUEST);
            }


            departmentRepo.deleteById(id);
            if (!departmentRepo.findById(id).isPresent()) {
                return new ResponseEntity<String>(String.format("Department %s has been deleted", temp.getYear() + "-" + temp.getEnumeration()), HttpStatus.OK);
            }

        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Department not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        if (departmentRepo.findById(id).isPresent()) {
            return new ResponseEntity<DepartmentEntity>(departmentRepo.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Department not found"), HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value ="/{id}/setHeadTeacher/{idt}", method = RequestMethod.PUT)
    public ResponseEntity<?> editHeadTeacher(@PathVariable Integer id, @PathVariable Integer idt){
        if (teachRepo.findById(idt).isPresent()) {
            if (departmentRepo.findById(id).isPresent()) {
                teachRepo.findById(idt).get().setHeadOfDepartment(departmentRepo.findById(id).get());
                teachRepo.save(teachRepo.findById(idt).get());
//                departmentRepo.findById(id).get().setHeadTeacher(teachRepo.findById(idt).get());
                return new ResponseEntity<DepartmentEntity>(departmentRepo.save(departmentRepo.findById(id).get()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Department or Teacher not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value ="/{id}/removeHeadTeacher", method = RequestMethod.PUT)
    public ResponseEntity<?> editHeadTeacher(@PathVariable Integer id){
        if(departmentRepo.findById(id).isPresent()){
            DepartmentEntity temp = departmentRepo.findById(id).get();
            if (temp.getHeadTeacher() != null){
                teachRepo.findById(temp.getHeadTeacher().getId()).get().setHeadOfDepartment(null);
                teachRepo.save(teachRepo.findById(temp.getHeadTeacher().getId()).get());
                return new ResponseEntity<DepartmentEntity>(departmentRepo.save(departmentRepo.findById(id).get()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Department not found or teacher not assigned"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value ="/{id}/removeReferences", method = RequestMethod.PUT)
    public ResponseEntity<?> removeReferences(@PathVariable Integer id){
        if (departmentRepo.findById(id).isPresent()){
            DepartmentEntity temp = departmentRepo.findById(id).get();
            if (temp.getHeadTeacher() != null){
                teachRepo.findById(temp.getHeadTeacher().getId()).get().setHeadOfDepartment(null);
                teachRepo.save(teachRepo.findById(temp.getHeadTeacher().getId()).get());

            }

            if (temp.getSchedule() != null && temp.getSchedule().size() >0){
                scheduleRepo.deleteAllByDepartment(temp);
            }

            if (temp.getPupils() != null && temp.getPupils().size() > 0){
                pupilRepo.deleteAllByDepartment(temp);
            }
            return new ResponseEntity<DepartmentEntity>(departmentRepo.save(departmentRepo.findById(id).get()), HttpStatus.OK);
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Department not found"), HttpStatus.NOT_FOUND);
    }
}
