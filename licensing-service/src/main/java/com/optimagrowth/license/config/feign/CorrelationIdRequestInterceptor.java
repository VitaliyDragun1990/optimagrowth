package com.optimagrowth.license.config.feign;

import com.optimagrowth.license.config.Constants.Headers;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class CorrelationIdRequestInterceptor implements RequestInterceptor {

    private final Supplier<String> correlationIdSupplier;

    @Override
    public void apply(RequestTemplate template) {
        template.header(Headers.CORRELATION_ID, correlationIdSupplier.get());
    }
}
