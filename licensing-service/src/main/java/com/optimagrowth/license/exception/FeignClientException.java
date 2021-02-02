package com.optimagrowth.license.exception;

public class FeignClientException extends ApplicationException {

    public FeignClientException(String message, Object... args) {
        super(message, args);
    }
}
