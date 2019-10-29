package com.meli.challenge.exception.model;

/**
 * Base class used for custom exceptions of the application.
 */
public abstract class CustomException extends Exception implements ExceptionWithStatusCode {

    CustomException(String message) {
        super(message);
    }
}
