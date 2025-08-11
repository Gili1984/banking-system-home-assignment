package com.banking.transaction_service.dto;

import com.banking.transaction_service.model.TransactionEnum;
import com.mongodb.client.model.changestream.OperationType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto  {
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private String currency;
    private TransactionEnum.TransactionType type;
    private String description;
}