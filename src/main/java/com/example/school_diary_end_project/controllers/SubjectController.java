package com.example.school_diary_end_project.controllers;

import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.SubjectEntity;
import com.example.school_diary_end_project.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepo;



    @RequestMapping
    public ResponseEntity<?> getDb() {
        return new ResponseEntity<List<SubjectEntity>>((List<SubjectEntity>) subjectRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addSubject(@RequestBody SubjectEntity newSubject) {
        return new ResponseEntity<SubjectEntity>(subjectRepo.save(newSubject), HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> editSubject(@RequestBody SubjectEntity subject, @PathVariable Integer id) {
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

            if (!subject.getDescription().equals(null)){
                temp.setDescription(subject.getDescription());
            }

            if (!subject.getYear().equals(null)){
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

            subjectRepo.deleteById(id);
            return new ResponseEntity<String>(String.format("Subject %s has been deleted",temp.getName()), HttpStatus.OK);
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



    // dodeljuje predmete razredima za koji su predvidjeni (1-4 razred)
//    @RequestMapping(method = RequestMethod.PUT, value = "/update")
//    public ResponseEntity<?> updateSubjectYear() {
//        List<SubjectEntity> lista = (List<SubjectEntity>) subjectRepo.findAll();
//        for (SubjectEntity subject : lista) {
//            if (subject.getPlanForYear().equals(null)) {
//                continue;
//            }
//
//            if (subject.getPlanForYear().equals(classRepo.findByYear(subject.getPlanForYear()).getYear())) {
//                subject.setClassYear(classRepo.findByYear(subject.getPlanForYear()));
//            }
//            subjectRepo.save(subject);
//
//        }
//
//        subjectRepo.saveAll(lista);
//
//        return new ResponseEntity<List<SubjectEntity>>((List<SubjectEntity>) subjectRepo.saveAll(lista), HttpStatus.OK);
//    }
}
