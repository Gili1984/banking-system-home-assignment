package com.banking.account_service.dto;

import com.banking.account_service.model.AccountEnums;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateAccountDto {
    private AccountEnums.OperationType operationType;
    private BigDecimal amount;
    private AccountEnums.AccountType accountType;
    private String currency;
    private AccountEnums.AccountStatus status;
}

