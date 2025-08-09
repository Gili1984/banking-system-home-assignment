package com.banking.transaction_service.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class DepositAndWithdrawDto {
    private String accountNumber;
    private BigDecimal amount;
    private String currency;
    private String description;
}


