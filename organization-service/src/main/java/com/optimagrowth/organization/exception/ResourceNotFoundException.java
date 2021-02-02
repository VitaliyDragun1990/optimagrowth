package com.optimagrowth.organization.exception;

public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String message, Object... args) {
        super(message, args);
    }
}
