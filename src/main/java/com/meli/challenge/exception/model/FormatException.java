package com.meli.challenge.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class FormatException extends CustomException {

    @Getter
    private final HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    public FormatException(String message) {
        super(message);
    }
}
