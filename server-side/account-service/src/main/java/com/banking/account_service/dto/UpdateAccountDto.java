package com.banking.account_service.dto;

import com.banking.account_service.model.AccountEnums;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateAccountDto {
    private AccountEnums.OperationType operationType;
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    private BigDecimal amount;
    private AccountEnums.AccountType accountType;
    private String currency;
    private AccountEnums.AccountStatus status;
}

