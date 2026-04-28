package com.solvia.userservice.application.exception;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super("User must be authenticated");
    }
}