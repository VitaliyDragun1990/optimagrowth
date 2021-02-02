package com.optimagrowth.license.exception;

import static java.lang.String.format;

public abstract class ApplicationException extends RuntimeException {

    public ApplicationException(String message, Object... args) {
        super(format(message, args));
    }

    public ApplicationException(Throwable cause, String message, Object... args) {
        super(format(message, args), cause);
    }
}
