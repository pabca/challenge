package com.meli.challenge.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {

    @Getter
    private final HttpStatus statusCode = HttpStatus.NOT_FOUND;

    public NotFoundException(String message) {
        super(message);
    }
}