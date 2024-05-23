package ru.ainur.bank.exception;

public class UserUpdateException extends BankOperationException {
    public UserUpdateException(String message) {
        super(message);
    }
}
