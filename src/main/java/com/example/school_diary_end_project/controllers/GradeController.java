package com.example.school_diary_end_project.controllers;

import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.GradeEntity;
import com.example.school_diary_end_project.entities.PupilEntity;
import com.example.school_diary_end_project.entities.SubjectEntity;
import com.example.school_diary_end_project.entities.TeacherEntity;
import com.example.school_diary_end_project.entities.dto.GradeDto;
import com.example.school_diary_end_project.repositories.GradeRepository;
import com.example.school_diary_end_project.repositories.PupilRepository;
import com.example.school_diary_end_project.repositories.SubjectRepository;
import com.example.school_diary_end_project.repositories.TeacherRepository;
import com.example.school_diary_end_project.services.EmailService;
import com.example.school_diary_end_project.services.ValidationMethods;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grades")
public class GradeController {



    @Autowired
    private GradeRepository gradeRepo;

    @Autowired
    private PupilRepository pupilRepo;

    @Autowired
    private TeacherRepository teachRepo;

    @Autowired
    private SubjectRepository subRepo;

    @Autowired
    private ValidationMethods validation;

    @Autowired
    private EmailService emailService;

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private String createErrorMessage(BindingResult result) {
        return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
    }

    @RequestMapping
    public ResponseEntity<?> getDb() {
        return new ResponseEntity<List<GradeEntity>>((List<GradeEntity>) gradeRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addGrade(@Valid @RequestBody GradeDto newGrade, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }

        GradeEntity graded = new GradeEntity();
        if (pupilRepo.findById(newGrade.getPupilId()).isPresent()
                && teachRepo.findById(newGrade.getProfessorId()).isPresent()
                && subRepo.findById(newGrade.getSubjectId()).isPresent()) {
            if (validation.checkForGrade(newGrade)) {
                graded.setGradeValue(newGrade.getValue());
                graded.setComment(newGrade.getComment());
                graded.setDateOfGrading(LocalDate.now());
                graded.setPupil(pupilRepo.findById(newGrade.getPupilId()).get());
                graded.setTeacher(teachRepo.findById(newGrade.getProfessorId()).get());
                graded.setSubject(subRepo.findById(newGrade.getSubjectId()).get());

                try {
                    emailService.sendTemplateMessage(graded);
                }catch (Exception e){
                    e.printStackTrace();
                }


                logger.info("Grade created");
                return new ResponseEntity<GradeEntity>(gradeRepo.save(graded), HttpStatus.OK);
            }
            return new ResponseEntity<RESTError>(new RESTError(1, "Unable to assign requested grade"),
                    HttpStatus.NOT_FOUND);

        }
        logger.info("Entity not found");
        return new ResponseEntity<RESTError>(new RESTError(1, "Pupil, Teacher or Subject not found"),
                HttpStatus.NOT_FOUND);

    }
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> editGrade(@Valid @RequestBody GradeEntity editGrade, @PathVariable Integer id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(createErrorMessage(bindingResult), HttpStatus.BAD_REQUEST);
        }
        if (gradeRepo.findById(id).isPresent()) {
            GradeEntity temp = gradeRepo.findById(id).get();
            temp.setComment(editGrade.getComment());
            temp.setGradeValue(editGrade.getGradeValue());
            temp.setGradeType(editGrade.getGradeType());
            return new ResponseEntity<GradeEntity>(gradeRepo.save(temp), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Grade not found"), HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteGrade(@PathVariable Integer id) {
        if (gradeRepo.findById(id).isPresent()) {
            GradeEntity temp = gradeRepo.findById(id).get();
            if (!temp.getPupil().equals(null)){
                for (PupilEntity pupil: pupilRepo.findAll()) {
                    pupil.getGrades().remove(temp);
                    pupilRepo.save(pupil);
                }
            }

            if (!temp.getSubject().equals(null)){
                for (SubjectEntity subject: subRepo.findAll()) {
                    subject.getGrades().remove(temp);
                    subRepo.save(subject);
                }
            }

            if (!temp.getTeacher().equals(null)){
                for (TeacherEntity teacher: teachRepo.findAll()) {
                    teacher.getGrades().remove(temp);
                    teachRepo.save(teacher);
                }
            }

            gradeRepo.deleteById(id);

            return new ResponseEntity<String>("Grade deleted", HttpStatus.OK);

        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Grade not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        if (gradeRepo.findById(id).isPresent()) {
            return new ResponseEntity<GradeEntity>(gradeRepo.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Grade not found"), HttpStatus.NOT_FOUND);
    }

}
