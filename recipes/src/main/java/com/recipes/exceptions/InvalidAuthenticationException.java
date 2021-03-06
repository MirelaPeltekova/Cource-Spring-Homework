package com.recipes.exceptions;

public class InvalidAuthenticationException extends RuntimeException {
    public InvalidAuthenticationException() {
    }

    public InvalidAuthenticationException(String message) {
        super(message);
    }

    public InvalidAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthenticationException(Throwable cause) {
        super(cause);
    }
}