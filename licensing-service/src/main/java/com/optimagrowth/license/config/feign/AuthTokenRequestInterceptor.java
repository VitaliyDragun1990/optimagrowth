package com.optimagrowth.license.config.feign;

import com.optimagrowth.license.config.Constants.Headers;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class AuthTokenRequestInterceptor implements RequestInterceptor {

    private final Supplier<String> authTokenSupplier;

    @Override
    public void apply(RequestTemplate template) {
        template.header(Headers.CORRELATION_ID, authTokenSupplier.get());
    }
}
