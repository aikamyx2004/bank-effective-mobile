package ru.ainur.bank.exception;

public class InvalidRefreshTokenException extends BankOperationException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
