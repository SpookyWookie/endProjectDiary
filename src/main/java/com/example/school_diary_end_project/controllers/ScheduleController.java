package com.example.school_diary_end_project.controllers;


import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.ScheduleEntity;
import com.example.school_diary_end_project.entities.dto.ScheduleDTO;
import com.example.school_diary_end_project.repositories.DepartmentRepository;
import com.example.school_diary_end_project.repositories.ScheduleRepository;
import com.example.school_diary_end_project.repositories.SubjectRepository;
import com.example.school_diary_end_project.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleRepository repo;

    @Autowired
    private SubjectRepository subRepo;

    @Autowired
    private TeacherRepository teachRepo;

    @Autowired
    private DepartmentRepository depRepo;

    private String createErrorMessage(BindingResult result) {
        return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
    }

    @RequestMapping
    public ResponseEntity<?> getDb(){
        return new ResponseEntity<List<ScheduleEntity>>((List<ScheduleEntity>)repo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addSchedule (@RequestBody ScheduleDTO newSchedule, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }

        ScheduleEntity temp = new ScheduleEntity();

        if (newSchedule.getDescription() != null){
            temp.setDescription(newSchedule.getDescription());
        }

        if (teachRepo.findById(newSchedule.getTeacherid()).isPresent()) {
            temp.setTeacher(teachRepo.findById(newSchedule.getTeacherid()).get());
        }
        else {
            return new ResponseEntity<RESTError>(new RESTError(1, "No teacher found"), HttpStatus.NOT_FOUND);
        }

        if (subRepo.findById(newSchedule.getSubjectid()).isPresent()) {
            temp.setSubject(subRepo.findById(newSchedule.getSubjectid()).get());
        }
        else {
            return new ResponseEntity<RESTError>(new RESTError(1, "No subject found"), HttpStatus.NOT_FOUND);
        }

        if (depRepo.findById(newSchedule.getDepartmentid()).isPresent()) {
            temp.setDepartment(depRepo.findById(newSchedule.getDepartmentid()).get());
        }
        else {
            return new ResponseEntity<RESTError>(new RESTError(1, "No department found"), HttpStatus.NOT_FOUND);
        }


        if (repo.existsByTeacherAndSubjectAndDepartment(temp.getTeacher(), temp.getSubject(), temp.getDepartment())) {
            return new ResponseEntity<RESTError>(new RESTError(1, "This entry combination already exists"), HttpStatus.ALREADY_REPORTED);
        }




        return new ResponseEntity<ScheduleEntity>(repo.save(temp), HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSchedule(@Valid  @RequestBody ScheduleDTO schedule, @PathVariable Integer id, BindingResult bindingResult){
        ScheduleEntity temp = repo.findById(id).get();

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }

        if (repo.findById(id).isPresent()) {
            if (!schedule.getDescription().equals(null)){
                temp.setDescription(schedule.getDescription());
            }

            if (schedule.getDepartmentid() != null) {
                if (depRepo.findById(schedule.getDepartmentid()).isPresent()){
                    temp.setDepartment(depRepo.findById(schedule.getDepartmentid()).get());
                }else {
                    return new ResponseEntity<RESTError>(new RESTError(2, "Department not found"), HttpStatus.NOT_FOUND);
                }

            }

            if (schedule.getSubjectid() != null) {
                if (subRepo.findById(schedule.getSubjectid()).isPresent()){
                    temp.setSubject(subRepo.findById(schedule.getSubjectid()).get());
                }else {
                    return new ResponseEntity<RESTError>(new RESTError(3, "Subject not found"), HttpStatus.NOT_FOUND);
                }
            }

            if (schedule.getTeacherid() != null) {
                if (teachRepo.findById(schedule.getTeacherid()).isPresent()){
                    temp.setTeacher(teachRepo.findById(schedule.getTeacherid()).get());
                }else {
                    return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found"), HttpStatus.NOT_FOUND);
                }
            }

            return new ResponseEntity<ScheduleEntity>(repo.save(temp), HttpStatus.OK );
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Schedule combination not found"), HttpStatus.NOT_FOUND);

    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteScheduleEntry(@PathVariable Integer id){
        if (repo.findById(id).isPresent()) {
            ScheduleEntity temp = repo.findById(id).get();
            if (!temp.getDepartment().equals(null)){
                depRepo.findBySchedule(temp).getSchedule().remove(temp);
                depRepo.save(temp.getDepartment());
            }
            if (!temp.getSubject().equals(null)){
                subRepo.findBySchedule(temp).getSchedule().remove(temp);
                subRepo.save(temp.getSubject());

            }
            if (!temp.getTeacher().equals(null)){
                teachRepo.findBySchedule(temp).getSchedule().remove(temp);
                teachRepo.save(temp.getTeacher());

            }

            repo.deleteById(id);
            return new ResponseEntity<RESTError>(new RESTError(1, "Schedule entry deleted"), HttpStatus.OK);
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Schedule entry not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findEntry(@PathVariable Integer id){
        if (repo.findById(id).isPresent()) {
            return new ResponseEntity<ScheduleEntity>(repo.findById(id).get(), HttpStatus.OK);
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Schedule entry not found"), HttpStatus.NOT_FOUND);
    }


}
