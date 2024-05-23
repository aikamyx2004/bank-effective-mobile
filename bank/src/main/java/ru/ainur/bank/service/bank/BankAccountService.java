package ru.ainur.bank.service.bank;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ainur.bank.dto.request.MoneyTransferRequest;
import ru.ainur.bank.dto.request.user.UserRegistrationRequest;
import ru.ainur.bank.exception.MoneyTransferException;
import ru.ainur.bank.exception.NegativeBalanceException;
import ru.ainur.bank.exception.UserUpdateException;
import ru.ainur.bank.model.BankAccount;
import ru.ainur.bank.model.User;
import ru.ainur.bank.repository.BankAccountRepository;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;

    @Transactional
    public void transferMoney(MoneyTransferRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new UserUpdateException("User is not authenticated");
        }

        User user = (User) authentication.getPrincipal();
        BankAccount fromAccount = user.getBankAccount();
        BankAccount toAccount = getBankAccount(request.getToAccountId());
        log.info("Received money transfer request from {} to {} amount {}", fromAccount.getId(), request.getToAccountId(), request.getAmount());
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new MoneyTransferException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
    }

    public BankAccount createBankAccountForUser(UserRegistrationRequest request, User newUser) {
        BankAccount bankAccount = createBankAccountFromUserRegistrationRequest(request, newUser);
        if (bankAccount.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeBalanceException("You can not create bank account with negative balance");
        }
        return bankAccountRepository.save(bankAccount);
    }

    private BankAccount getBankAccount(long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid from account ID=%d".formatted(id))
                );
    }

    private static BankAccount createBankAccountFromUserRegistrationRequest(UserRegistrationRequest request, User newUser) {
        return BankAccount.builder()
                .user(newUser)
                .initialDeposit(request.getInitialDeposit())
                .balance(request.getInitialDeposit())
                .build();
    }
}
