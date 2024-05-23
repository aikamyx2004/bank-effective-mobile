package ru.ainur.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.ainur.bank.dto.request.MoneyTransferRequest;
import ru.ainur.bank.dto.request.user.UserRegistrationRequest;
import ru.ainur.bank.exception.MoneyTransferException;
import ru.ainur.bank.exception.NegativeBalanceException;
import ru.ainur.bank.exception.UserUpdateException;
import ru.ainur.bank.model.BankAccount;
import ru.ainur.bank.model.User;
import ru.ainur.bank.repository.BankAccountRepository;
import ru.ainur.bank.service.bank.BankAccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BankAccountServiceTests {


    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private BankAccountService bankAccountService;

    private BankAccount fromAccount;
    private BankAccount toAccount;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1L).build();

        fromAccount = BankAccount.builder()
                .id(1L)
                .balance(new BigDecimal("1000.00"))
                .user(user)
                .build();
        user.setBankAccount(fromAccount);

        toAccount = BankAccount.builder()
                .id(2L)
                .balance(new BigDecimal("500.00"))
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.isAuthenticated()).thenReturn(true);
    }

    @Test
    void transferMoney_Success() {
        MoneyTransferRequest request = MoneyTransferRequest.builder()
                .toAccountId(2L)
                .amount(new BigDecimal("100.00"))
                .build();

        when(bankAccountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        bankAccountService.transferMoney(request);

        assertEquals(new BigDecimal("900.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("600.00"), toAccount.getBalance());

        verify(bankAccountRepository).save(fromAccount);
        verify(bankAccountRepository).save(toAccount);
    }

    @Test
    void transferMoney_InsufficientBalance() {
        MoneyTransferRequest request = MoneyTransferRequest.builder()
                .toAccountId(2L)
                .amount(new BigDecimal("1100.00"))
                .build();

        when(bankAccountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        MoneyTransferException exception = assertThrows(MoneyTransferException.class, () -> bankAccountService.transferMoney(request));
        assertEquals("Insufficient balance", exception.getMessage());

        assertEquals(new BigDecimal("1000.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("500.00"), toAccount.getBalance());

        verify(bankAccountRepository, never()).save(fromAccount);
        verify(bankAccountRepository, never()).save(toAccount);
    }

    @Test
    void transferMoney_AccountNotFound() {
        MoneyTransferRequest request = MoneyTransferRequest.builder()
                .toAccountId(3L)
                .amount(new BigDecimal("100.00"))
                .build();

        when(bankAccountRepository.findById(3L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bankAccountService.transferMoney(request));
        assertEquals("Invalid from account ID=3", exception.getMessage());

        assertEquals(new BigDecimal("1000.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("500.00"), toAccount.getBalance());

        verify(bankAccountRepository, never()).save(fromAccount);
        verify(bankAccountRepository, never()).save(toAccount);
    }

    @Test
    void transferMoney_UserNotAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(false);

        MoneyTransferRequest request = MoneyTransferRequest.builder()
                .toAccountId(2L)
                .amount(new BigDecimal("100.00"))
                .build();

        UserUpdateException exception = assertThrows(UserUpdateException.class, () -> bankAccountService.transferMoney(request));
        assertEquals("User is not authenticated", exception.getMessage());

        assertEquals(new BigDecimal("1000.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("500.00"), toAccount.getBalance());

        // Remove unnecessary stubbing
        verify(bankAccountRepository, never()).save(fromAccount);
        verify(bankAccountRepository, never()).save(toAccount);
    }

    @Test
    void createBankAccountForUser_NegativeInitialDeposit() {
        User newUser = User.builder().id(2L).build();

        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .username("testuser")
                .password("password")
                .fullName("Test User")
                .email("test@example.com")
                .phone("+1234567890")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .initialDeposit(new BigDecimal("-100.00"))
                .build();

        NegativeBalanceException exception = assertThrows(NegativeBalanceException.class, () -> bankAccountService.createBankAccountForUser(request, newUser));
        assertEquals("You can not create bank account with negative balance", exception.getMessage());

        verify(bankAccountRepository, never()).save(any(BankAccount.class));
    }
}