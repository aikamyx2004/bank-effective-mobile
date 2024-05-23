package ru.ainur.bank.exception;

public class NegativeBalanceException extends BankOperationException {
    public NegativeBalanceException(String message) {
        super(message);
    }
}
