package ru.ainur.bank.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(JwtException e) {
        log.error("Exception on handling JWT occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        log.error("Validation errors occurred: {}", errors);
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(UsernameNotFoundException e) {
        log.error("Username not found: {}", e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> onAuthenticationFailure(AuthenticationException e) {
        log.error("Authentication failure occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BankOperationException.class)
    public ResponseEntity<String> handleBankOperationException(BankOperationException e) {
        log.error("Bank operation exception occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> ex(Exception e) {
        log.error("Unhandled exception occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
