package com.example.school_diary_end_project.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileHandler {

    public @ResponseBody ResponseEntity<?> logDownload() throws IOException;

    public @ResponseBody byte[] getFile() throws IOException;

    public /*void*/ ResponseEntity getDownload(HttpServletResponse response) throws IOException;


}
