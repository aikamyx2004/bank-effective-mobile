package ru.ainur.bank.service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ainur.bank.dto.response.JwtAuthenticationResponse;
import ru.ainur.bank.exception.InvalidRefreshTokenException;
import ru.ainur.bank.model.RefreshToken;
import ru.ainur.bank.model.User;
import ru.ainur.bank.repository.RefreshTokenRepository;
import ru.ainur.bank.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationResponse refreshToken(String token) {
        return findByToken(token)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUsername());
                    return JwtAuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .token(token)
                            .build();
                }).orElseThrow(() -> new InvalidRefreshTokenException(
                        "Refresh Token is not in DB, token='%s'".formatted(token)
                ));
    }

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: %s".formatted(username))
                );

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshToken());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(jwtExpiration));

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new InvalidRefreshTokenException(
                    "'%s' Refresh token is expired. Please make a new login..!".formatted(token.getToken())
            );
        }
        return token;
    }
}
