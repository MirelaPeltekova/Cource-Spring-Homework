package com.recipes.exceptions;

public class NonExistingentityException extends RuntimeException {
    public NonExistingentityException() {
    }

    public NonExistingentityException(String message) {
        super(message);
    }

    public NonExistingentityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistingentityException(Throwable cause) {
        super(cause);
    }
}

