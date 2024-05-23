package ru.ainur.bank.service.user;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.ainur.bank.dto.request.user.UserAuthenticationRequest;
import ru.ainur.bank.dto.request.user.UserRegistrationRequest;
import ru.ainur.bank.dto.response.BankAccountDto;
import ru.ainur.bank.dto.response.JwtAuthenticationResponse;
import ru.ainur.bank.dto.response.UserDto;
import ru.ainur.bank.model.BankAccount;
import ru.ainur.bank.model.RefreshToken;
import ru.ainur.bank.model.User;
import ru.ainur.bank.service.bank.BankAccountService;
import ru.ainur.bank.service.jwt.JwtService;
import ru.ainur.bank.service.jwt.RefreshTokenService;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public UserDto register(UserRegistrationRequest request) {
        User user = userService.createUser(request);

        BankAccount bankAccount = bankAccountService.createBankAccountForUser(request, user);

        BankAccountDto bankAccountDto = toDtoBankAccount(bankAccount);

        return createUserRegistrationResponse(user, bankAccountDto);
    }

    public JwtAuthenticationResponse login(UserAuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());

        return JwtAuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(request.getUsername()))
                .token(refreshToken.getToken())
                .build();
    }

    private static UserDto createUserRegistrationResponse(User user, BankAccountDto bankAccountDto) {
        return UserDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .bankAccountDto(bankAccountDto)
                .build();
    }

    private static BankAccountDto toDtoBankAccount(BankAccount bankAccount) {
        return BankAccountDto.builder()
                .id(bankAccount.getId())
                .initialDeposit(bankAccount.getInitialDeposit())
                .balance(bankAccount.getBalance())
                .build();
    }
}