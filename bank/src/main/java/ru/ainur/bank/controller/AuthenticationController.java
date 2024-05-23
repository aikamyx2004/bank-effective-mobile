package ru.ainur.bank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ainur.bank.dto.request.RefreshTokenRequest;
import ru.ainur.bank.dto.request.user.UserAuthenticationRequest;
import ru.ainur.bank.dto.request.user.UserRegistrationRequest;
import ru.ainur.bank.dto.response.JwtAuthenticationResponse;
import ru.ainur.bank.dto.response.UserDto;
import ru.ainur.bank.service.user.AuthenticationService;
import ru.ainur.bank.service.jwt.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Аутентификация")
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "Регистрация пользователя")
    @Transactional
    @PostMapping("/register")
    public UserDto registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        log.info("Received registration request for user: {}", request.getUsername());
        return authenticationService.register(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody @Valid UserAuthenticationRequest request) {
        log.info("Received login request for user: {}", request.getUsername());
        return authenticationService.login(request);
    }

    @Operation(summary = "Обновление JWT токена")
    @PostMapping("/refreshToken")
    public JwtAuthenticationResponse refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequestDTO) {
        log.info("Received refresh token request");
        return refreshTokenService.refreshToken(refreshTokenRequestDTO.getToken());
    }
}