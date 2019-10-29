package com.meli.challenge.exception.handler;

import com.meli.challenge.exception.model.CustomException;
import com.meli.challenge.resource.ErrorResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handler for the application.
 */
@Slf4j
@ControllerAdvice
public class ResponseExceptionHandler {

    /**
     * Handles custom exceptions of the application.
     *
     * @param exception custom exception to handle.
     * @return error resource.
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResource> handleCustomException(CustomException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorResource(exception.getMessage()), exception.getStatusCode());
    }

    /**
     * Handles generic exceptions of the application. Status code 500 is returned.
     *
     * @param exception generic exception to handle.
     * @return error resource.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResource> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorResource(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
