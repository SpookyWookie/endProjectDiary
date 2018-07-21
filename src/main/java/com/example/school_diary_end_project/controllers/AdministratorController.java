package com.example.school_diary_end_project.controllers;


import com.example.school_diary_end_project.controllers.util.RESTError;
import com.example.school_diary_end_project.entities.AdministratorEntity;
import com.example.school_diary_end_project.entities.UserEntity;
import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.example.school_diary_end_project.repositories.AdministratorRepository;
import com.example.school_diary_end_project.repositories.UserRepository;


import com.example.school_diary_end_project.services.FileHandler;
import com.mysql.cj.x.protobuf.Mysqlx;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ByteArrayResource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.print.attribute.standard.Media;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
//import java.nio.file.Path;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/administrators")
public class AdministratorController {

//    @Autowired
//    private Logger logLogger = LoggerFactory.getLogger(AdministratorController.class);

    @Autowired
    private FileHandler fileHandler;


    @Autowired
    private AdministratorRepository adminRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping
    public ResponseEntity<?> getDb(){
        return new ResponseEntity<List<AdministratorEntity>>((List<AdministratorEntity>)adminRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addAdministrator(@RequestBody AdministratorEntity admin){
        List<EUserRole> list = new ArrayList<>();
        list.add(EUserRole.ROLE_ADMINISTRATOR);
        admin.setRoles(list);
        if (admin.getPassword() != null){
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }

        return new ResponseEntity<AdministratorEntity>(adminRepo.save(admin), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<?> editAdministrator(@RequestBody AdministratorEntity admin, @PathVariable Integer id) {
        if (adminRepo.findById(id).isPresent()) {
            AdministratorEntity temp = adminRepo.findById(id).get();

            if (!admin.getBirthdate().equals(null)) {

                temp.setBirthdate(admin.getBirthdate());
            }

            if (!admin.getEmail().equals(null)) {
                temp.setEmail(admin.getEmail());
            }

            if (!admin.getJmbg().equals(null)) {
                temp.setJmbg(admin.getJmbg());
            }

            if (!admin.getName().equals(null)) {
                temp.setName(admin.getName());
            }

            if (!admin.getSurname().equals(null)) {
                temp.setSurname(admin.getSurname());
            }

            if (!admin.getUsername().equals(null)) {
                temp.setUsername(admin.getUsername());
            }

            return new ResponseEntity<AdministratorEntity>(adminRepo.save(temp), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Administrator not found"), HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParent(@PathVariable Integer id){
        if (adminRepo.findById(id).isPresent()) {
            AdministratorEntity temp = adminRepo.findById(id).get();

            adminRepo.deleteById(id);
            return new ResponseEntity<AdministratorEntity>(temp, HttpStatus.OK);

        }

        return new ResponseEntity<RESTError>(new RESTError(1, "Administrator not found"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<?> findById (@PathVariable Integer id){
        if (adminRepo.findById(id).isPresent()) {
            return new ResponseEntity<AdministratorEntity>(adminRepo.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<RESTError>(new RESTError(1, "Administrator not found"), HttpStatus.NOT_FOUND);
    }
//TODO RADE SVE 3 DOWNLOAD METODE MAMU IM JEBEM, POSTMAN JE GOVNO!!!
    @RequestMapping(value = "/log", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody  ResponseEntity<?> logDownload() throws IOException{
        return fileHandler.logDownload();

//        File f = new File("logs/spring-boot-logging.log");
//
//        if (f.exists() && f.canRead() && f.isFile()) {
//            Path path = Paths.get(f.getAbsolutePath());
//            byte[] mock = new byte[0];
//            ByteArrayResource resource = new ByteArrayResource(mock);
//            try {
//                resource = new ByteArrayResource(Files.readAllBytes(path));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + f.getName()+".log");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(f.length())
//                    .contentType(MediaType.parseMediaType("text/plain"))
//                    .body(resource);
//
//
//
//        }
//        return new ResponseEntity<RESTError>(new RESTError(10, "File not found"), HttpStatus.NOT_FOUND);

    }


//TODO This returns a page that loads log, for a file rename return value to byte[]
    @RequestMapping(value = "/log1", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody byte[] /*ResponseEntity*/ getFile() throws IOException {
        InputStream in = new FileInputStream(new File("logs/spring-boot-logging.log"));
        return IOUtils.toByteArray(in);

        /*return new ResponseEntity(fileHandler.getFile(),HttpStatus.OK);*/
    }


    @RequestMapping(value="download", method=RequestMethod.GET)
    public /*void */ ResponseEntity getDownload(HttpServletResponse response) throws IOException {

//        InputStream myStream = new FileInputStream(new File("logs/spring-boot-logging.log"));
//
//        // Set the content type and attachment header.
//        response.addHeader("Content-disposition", "attachment;filename=spring-boot-logger.txt");
//        response.setContentType("text/plain");
//
//        // Copy the stream to the response's output stream.
//        IOUtils.copy(myStream, response.getOutputStream());
//        response.flushBuffer();
        return  new ResponseEntity(fileHandler.getDownload(response),HttpStatus.OK);
    }


}
