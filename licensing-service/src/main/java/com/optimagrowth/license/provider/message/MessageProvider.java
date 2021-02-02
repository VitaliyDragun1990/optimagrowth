package com.optimagrowth.license.provider.message;

import java.util.Locale;

public interface MessageProvider {

    String message(String key, Locale locale, Object... args);

    String message(String key, Object... args);
}
