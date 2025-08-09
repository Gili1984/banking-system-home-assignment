package com.banking.transaction_service.dto;

import com.banking.transaction_service.model.TransactionEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateAccountDto {
    private TransactionEnum.TransactionType operationType;
    private BigDecimal amount;
}
