package com.optimagrowth.license.config.feign;

import com.optimagrowth.license.exception.FeignClientException;
import com.optimagrowth.license.exception.ResourceNotFoundException;
import com.optimagrowth.license.usercontext.UserContextHolder;
import feign.RequestInterceptor;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class OrganizationServiceFeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ServiceClientErrorDecoder();
    }

    @Bean
    public RequestInterceptor clientIdRequestInterceptor() {
        return new CorrelationIdRequestInterceptor(() -> UserContextHolder.getContext().getCorrelationId());
    }

    @Bean
    public RequestInterceptor authTokenRequestInterceptor() {
        return new AuthTokenRequestInterceptor(() -> UserContextHolder.getContext().getAuthToken());
    }

    public static class ServiceClientErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {

            if (response.status() == HttpStatus.NOT_FOUND.value()) {
                return new ResourceNotFoundException("Organization with provided id not found");
            }

            return new FeignClientException("Error calling organization-service:[%s]", response.body().toString());
        }
    }

}
