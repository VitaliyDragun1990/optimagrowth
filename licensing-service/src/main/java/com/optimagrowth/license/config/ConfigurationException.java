package com.optimagrowth.license.config;

import com.optimagrowth.license.exception.ApplicationException;

public class ConfigurationException extends ApplicationException {

    public ConfigurationException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
