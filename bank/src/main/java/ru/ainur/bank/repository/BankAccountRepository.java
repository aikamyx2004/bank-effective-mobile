package ru.ainur.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ainur.bank.model.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
