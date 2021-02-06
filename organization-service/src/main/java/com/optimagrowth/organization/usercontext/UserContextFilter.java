package com.optimagrowth.organization.usercontext;

import com.optimagrowth.organization.config.Constants.Headers;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class UserContextFilter extends OncePerRequestFilter {

    private static final String PREFIX_BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOG.debug("Intercepting new user request and setting UserContext instance....");

        String correlationId = request.getHeader(Headers.CORRELATION_ID);
        String userId = request.getHeader(Headers.USER_ID);
        String authToken = getAuthTokenFrom(request.getHeader(HttpHeaders.AUTHORIZATION));
        String organizationId = request.getHeader(Headers.ORGANIZATION_ID);

        UserContextHolder.setContext(new UserContext(correlationId, authToken, userId, organizationId));

        LOG.debug("UserContext has been set:{}", UserContextHolder.getContext());

        filterChain.doFilter(request, response);
    }

    private String getAuthTokenFrom(String headerValue) {
        if (headerValue.startsWith(PREFIX_BEARER)) {
            return headerValue.substring(PREFIX_BEARER.length());
        }
        return null;
    }
}
