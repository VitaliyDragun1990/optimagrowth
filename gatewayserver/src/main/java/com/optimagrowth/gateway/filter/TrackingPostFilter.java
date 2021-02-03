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

@Slf4j
@RequiredArgsConstructor
@Component
@Order(2)
public class TrackingPostFilter implements GlobalFilter {

    private final WebExchangeHandler webExchangeHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    String correlationId = webExchangeHandler.getRequestHeader(exchange, Headers.CORRELATION_ID).orElseThrow();

                    LOG.debug("Adding the correlation id to the outbound headers: {}", correlationId);

                    webExchangeHandler.setResponseHeader(exchange, Headers.CORRELATION_ID, correlationId);

                    LOG.debug("Complete outgoing request for {}", exchange.getRequest().getURI());
                }));
    }
}
