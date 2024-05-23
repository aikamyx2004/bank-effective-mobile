package ru.ainur.bank.exception;

public class MoneyTransferException extends BankOperationException {
    public MoneyTransferException(String message) {
        super(message);
    }
}
