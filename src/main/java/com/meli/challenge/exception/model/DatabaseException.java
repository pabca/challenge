package com.meli.challenge.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class DatabaseException extends CustomException {

    @Getter
    private final HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

    public DatabaseException(String message) {
        super(message);
    }
}
