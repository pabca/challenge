package com.meli.challenge.exception.model;

import org.springframework.http.HttpStatus;

public interface ExceptionWithStatusCode {

    HttpStatus getStatusCode();
    String getMessage();
}
