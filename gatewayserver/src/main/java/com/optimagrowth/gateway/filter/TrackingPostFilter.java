package com.optimagrowth.gateway.filter;

import com.optimagrowth.gateway.config.Constants.Headers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(2)
public class TrackingPostFilter implements GlobalFilter {

    private final WebExchangeHandler webExchangeHandler;

    private final Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    Optional<String> traceIdOptional = findCurrentTraceId();

                    if (traceIdOptional.isPresent()) {
                        String traceId = traceIdOptional.get();

                        LOG.debug("Adding the sleuth traceId as correlation id to the outbound headers: {}", traceId);

                        webExchangeHandler.setResponseHeader(exchange, Headers.CORRELATION_ID, traceId);

                        LOG.debug("Complete outgoing request for {}", exchange.getRequest().getURI());
                    } else {
                        LOG.warn("Fail to find current Spring Cloud Sleuth TraceId value");
                    }

                }));
    }

    private Optional<String> findCurrentTraceId() {
        return Optional.ofNullable(tracer.currentSpan())
                .map(Span::context)
                .map(TraceContext::spanId);
    }
}
