package com.optimagrowth.license.exception;

public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String message, Object... args) {
        super(message, args);
    }
}
