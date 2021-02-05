package com.optimagrowth.license.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class AuthTokenRequestInterceptor implements RequestInterceptor {

    private static final String PREFIX_BEARER = "Bearer ";

    private final Supplier<String> authTokenSupplier;

    @Override
    public void apply(RequestTemplate template) {
        template.header(HttpHeaders.AUTHORIZATION, PREFIX_BEARER + authTokenSupplier.get());
    }
}
