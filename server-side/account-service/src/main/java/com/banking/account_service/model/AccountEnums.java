package com.banking.account_service.model;

public class AccountEnums {
    public enum AccountType {
        SAVINGS,
        CHECKING
    }

    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        BLOCKED
    }
    public enum OperationType {
        DEPOSIT,
        WITHDRAWAL
    }
}
