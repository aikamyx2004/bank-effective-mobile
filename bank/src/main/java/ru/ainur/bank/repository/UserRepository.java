package ru.ainur.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.ainur.bank.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);
}