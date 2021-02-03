package com.optimagrowth.license.usercontext;

import com.optimagrowth.license.config.Constants.Headers;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOG.debug("Intercepting new user request and setting UserContext instance....");

        String correlationId = request.getHeader(Headers.CORRELATION_ID);
        String userId = request.getHeader(Headers.USER_ID);
        String authToken = request.getHeader(Headers.AUTH_TOKEN);
        String organizationId = request.getHeader(Headers.ORGANIZATION_ID);

        UserContextHolder.setContext(new UserContext(correlationId, authToken, userId, organizationId));

        LOG.debug("UserContext has been set:{}", UserContextHolder.getContext());

        filterChain.doFilter(request, response);
    }
}
