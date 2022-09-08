package com.prototype.natlexservice.exception;

public class NotAuthorizationException extends RuntimeException{

    public NotAuthorizationException() {
        super("Not authenticated");
    }

    public NotAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

}
