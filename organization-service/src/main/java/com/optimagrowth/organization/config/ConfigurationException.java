package com.optimagrowth.organization.config;

import com.optimagrowth.organization.exception.ApplicationException;

public class ConfigurationException extends ApplicationException {

    public ConfigurationException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
