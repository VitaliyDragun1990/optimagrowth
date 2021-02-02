package com.optimagrowth.license.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public final class WebUtil {

    private WebUtil() {
    }

    public static String getCurrentRequestFullUri() {
        ServletUriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        return decode(uriBuilder.toUriString());
    }

    public static String getCurrentRequestPath() {
        ServletUriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        return decode(uriBuilder.build().getPath());
    }

    public static String getCurrentRequestPath(HttpServletRequest request) {
        ServletUriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromRequestUri(request);
        return decode(uriBuilder.build().getPath());
    }

    private static String decode(String uriString) {
        try {
            return URLDecoder.decode(uriString, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Error decoding URI string:[" + uriString + "]", e);
        }
    }
}
