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

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(1)
public class TrackingPreFilter implements GlobalFilter {

    private static final String PAYLOAD_USERNAME = "preferred_username";

    private static final String PREFIX_BEARER = "Bearer ";

    private final WebExchangeHandler webExchangeHandler;

    private final TokenParser tokenParser;

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

        LOG.debug("The authentication name from token is: {}", getUsername(exchange));

        return chain.filter(exchange);
    }

    private String getUsername(ServerWebExchange exchange) {
        Optional<String> authHeaderOptional = webExchangeHandler.getRequestHeader(exchange, Headers.AUTH_TOKEN);

        if (authHeaderOptional.isPresent()) {
            String authToken = authHeaderOptional.get().replace(PREFIX_BEARER, "");
            try {
                Map<String, Object> tokenPayload = tokenParser.parse(authToken);
                return tokenPayload.get(PAYLOAD_USERNAME).toString();
            } catch (Exception e) {
                LOG.warn("Error parsing access token payload:{}", e.getMessage(), e);
            }
        }

        return null;
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
