package com.example.school_diary_end_project.controllers;

import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.SubjectEntity;
import com.example.school_diary_end_project.repositories.GradeRepository;
import com.example.school_diary_end_project.repositories.ScheduleRepository;
import com.example.school_diary_end_project.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private GradeRepository gradeRepo;

    @Autowired
    private ScheduleRepository scheduleRepo;

    private String createErrorMessage(BindingResult result) {
        return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
    }


    @RequestMapping
    public ResponseEntity<?> getDb() {
        return new ResponseEntity<List<SubjectEntity>>((List<SubjectEntity>) subjectRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addSubject(@Valid @RequestBody SubjectEntity newSubject, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<SubjectEntity>(subjectRepo.save(newSubject), HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> editSubject(@Valid @RequestBody SubjectEntity subject, @PathVariable Integer id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }
        if (subjectRepo.findById(id).isPresent()) {
            SubjectEntity temp = subjectRepo.findById(id).get();
            if (!subject.getLessonsInAWeek().equals(null)) {
                temp.setLessonsInAWeek(subject.getLessonsInAWeek());
            }

            if (!subject.getName().equals(null)) {
                temp.setName(subject.getName());
            }

            if (!subject.getSemesterlessonsfund().equals(null)) {
                temp.setSemesterlessonsfund(subject.getSemesterlessonsfund());
            }

            if (!subject.getDescription().equals(null)) {
                temp.setDescription(subject.getDescription());
            }

            if (!subject.getYear().equals(null)) {
                temp.setYear(subject.getYear());
            }
            return new ResponseEntity<SubjectEntity>(subjectRepo.save(subjectRepo.findById(id).get()), HttpStatus.OK);

        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Subject not found"), HttpStatus.NOT_FOUND);

    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSubject(@PathVariable Integer id) {
        if (subjectRepo.findById(id).isPresent()) {
            SubjectEntity temp = subjectRepo.findById(id).get();
            if (temp.getGrades().size() > 0) {
                return new ResponseEntity<RESTError>(new RESTError(1, String.format
                        ("Subject %s cannot be deleted, remove its references to all grades beforehand", temp.getName())),
                        HttpStatus.BAD_REQUEST);
            }

            if (temp.getSchedule().size() > 0) {
                return new ResponseEntity<RESTError>(new RESTError(1, String.format
                        ("Subject %s cannot be deleted, remove its references to all schedule instances beforehand"
                                , temp.getName())), HttpStatus.BAD_REQUEST);
            }

            subjectRepo.deleteById(id);
            return new ResponseEntity<String>(String.format("Subject %s has been deleted", temp.getName()), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Subject not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        if (subjectRepo.findById(id).isPresent()) {
            return new ResponseEntity<SubjectEntity>(subjectRepo.findById(id).get(), HttpStatus.OK);

        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Subject not found"), HttpStatus.NOT_FOUND);

    }


    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/removeReferences")
    public ResponseEntity<?> removeReferences(@PathVariable Integer id){
        if (subjectRepo.findById(id).isPresent()){
            SubjectEntity temp = subjectRepo.findById(id).get();

            if (temp.getGrades().size() > 0 ){
                gradeRepo.deleteAllBySubject(temp);
            }

            if (temp.getSchedule().size()>0){
                scheduleRepo.deleteAllBySubject(temp);
            }

            return new ResponseEntity<SubjectEntity>(subjectRepo.findById(id).get(), HttpStatus.OK);
        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Subject not found"), HttpStatus.NOT_FOUND);
    }


}
