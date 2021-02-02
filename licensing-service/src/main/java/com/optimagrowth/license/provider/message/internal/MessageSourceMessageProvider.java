package com.optimagrowth.license.provider.message.internal;

import com.optimagrowth.license.provider.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageSourceMessageProvider implements MessageProvider {

    private final MessageSource messageSource;

    @Override
    public String message(String key, Locale locale, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }

    @Override
    public String message(String key, Object... args) {
        return messageSource.getMessage(key, args, null);
    }
}
