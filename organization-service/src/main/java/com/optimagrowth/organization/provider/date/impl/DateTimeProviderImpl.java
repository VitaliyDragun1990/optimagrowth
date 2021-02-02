package com.optimagrowth.organization.provider.date.impl;

import com.optimagrowth.organization.provider.date.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateTimeProviderImpl implements DateTimeProvider {

    @Override
    public LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }
}
