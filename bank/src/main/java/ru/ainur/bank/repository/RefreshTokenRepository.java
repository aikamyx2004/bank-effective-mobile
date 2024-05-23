package ru.ainur.bank.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import ru.ainur.bank.model.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(UserDetails userDetails);
}
