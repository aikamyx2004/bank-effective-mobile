package ru.ainur.bank.config.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Qualifier("handlerExceptionResolver")
    @Autowired
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.error("Exception on handling JWT occurred: {}", e.getMessage());
            resolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            log.error("Spring Security Filter Chain Exception:", e);
            resolver.resolveException(request, response, null, e);
        }
    }
}
