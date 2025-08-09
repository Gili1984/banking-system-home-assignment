package com.banking.account_service.exceptions;

public class AccountNotFoundException extends RuntimeException {
    private Number errorNumber;
    public AccountNotFoundException(String accountNumber) {
        super("Account with number " + accountNumber + " not found");
        errorNumber=2;
    }
}