package com.optimagrowth.gateway.filter;

import com.optimagrowth.gateway.config.Constants.Headers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(1)
public class TrackingPreFilter implements GlobalFilter {

    private final WebExchangeHandler webExchangeHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Optional<String> correlationIdOptional = findCorrelationId(exchange);

        if (correlationIdOptional.isPresent()) {
            String correlationId = correlationIdOptional.get();
            LOG.debug("{} found in tracking filter:{}", Headers.CORRELATION_ID, correlationId);
        } else {
            String correlationId = generateCorrelationId();
            exchange = setCorrelationId(exchange, correlationId);
            LOG.debug("{} generated in tracking filter: {}", Headers.CORRELATION_ID, correlationId);
        }

        return chain.filter(exchange);
    }

    private ServerWebExchange setCorrelationId(ServerWebExchange webExchange, String correlationId) {
        return this.webExchangeHandler.setRequestHeader(webExchange, Headers.CORRELATION_ID, correlationId);
    }

    private Optional<String> findCorrelationId(ServerWebExchange webExchange) {
        return webExchangeHandler.getRequestHeader(webExchange, Headers.CORRELATION_ID);
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
