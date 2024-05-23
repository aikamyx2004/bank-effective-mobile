package ru.ainur.bank.exception;

public abstract class BankOperationException extends RuntimeException {
    public BankOperationException(String message) {
        super(message);
    }
}
