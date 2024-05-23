package ru.ainur.bank.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Throwable cause = authException.getCause();
        response.setContentType("application/json");
        String message = cause == null ? "" : cause.getMessage();
        if (cause instanceof ExpiredJwtException) {
            log.error("Token has expired: {}", message);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{ \"error\": \"Token has expired: " + message + "\" }");
        } else {
            log.error("Attempt to make request when they are unauthorized: {}", message);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{ \"error\": \"You are unauthorized: " + message + "\" }");
        }
    }
}