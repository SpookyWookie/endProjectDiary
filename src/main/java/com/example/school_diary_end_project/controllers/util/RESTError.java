package com.example.school_diary_end_project.controllers.util;

public class RESTError {

    //@JsonView(Views.Public.class)
    private Integer id;

    //@JsonView(Views.Public.class)
    private String message;

    public Integer getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }


    public RESTError(Integer id, String message) {
        super();
        this.id = id;
        this.message = message;
    }
}
