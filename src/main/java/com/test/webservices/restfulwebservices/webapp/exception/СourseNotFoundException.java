package com.test.webservices.restfulwebservices.webapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class СourseNotFoundException extends RuntimeException {

    public СourseNotFoundException(String message) {
        super(message);
    }
}
