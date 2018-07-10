package com.example.school_diary_end_project.services;

import com.example.school_diary_end_project.controllers.util.RESTError;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileHandlerImpl implements FileHandler {

    public @ResponseBody ResponseEntity<?> logDownload() throws IOException {

        File f = new File("logs/spring-boot-logging.log");
        Path path = Paths.get(f.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        if (f.exists() && f.canRead() && f.isFile()) {


            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + f.getName() + ".log");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(f.length())
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .body(resource);


        }
        return new ResponseEntity<RESTError>(new RESTError(10, "File not found"), HttpStatus.NOT_FOUND);

    }

    public @ResponseBody byte[] getFile() throws IOException {
        InputStream in = new FileInputStream(new File("logs/spring-boot-logging.log"));
        return IOUtils.toByteArray(in);
    }

    public /*void*/ ResponseEntity getDownload(HttpServletResponse response) throws IOException {

        InputStream myStream = new FileInputStream(new File("logs/spring-boot-logging.log"));

        // Set the content type and attachment header.
        response.addHeader("Content-disposition", "attachment;filename=spring-boot-logger.txt");
        response.setContentType("text/plain");

        // Copy the stream to the response's output stream.
        IOUtils.copy(myStream, response.getOutputStream());
        response.flushBuffer();
        return new ResponseEntity(HttpStatus.OK);

    }

}
