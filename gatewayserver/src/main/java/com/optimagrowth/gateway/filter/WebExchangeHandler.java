package com.optimagrowth.gateway.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Optional;

@Component
public class WebExchangeHandler {

    public Optional<String> getRequestHeader(ServerWebExchange webExchange, String headerName) {
        HttpHeaders requestHeaders = webExchange.getRequest().getHeaders();

        List<String> headerValues = requestHeaders.get(headerName);
        if (headerValues != null) {
            return headerValues.stream().findFirst();
        }
        return Optional.empty();
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange webExchange, String headerName, String headerValue) {
        return webExchange.mutate().request(
                webExchange.getRequest().mutate()
                        .header(headerName, headerValue)
                        .build()
        ).build();
    }

    public ServerWebExchange setResponseHeader(ServerWebExchange webExchange, String headerName, String headerValue) {
        webExchange.getResponse().getHeaders().add(headerName, headerValue);
        return webExchange;
    }
}
