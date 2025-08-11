package com.banking.transaction_service.dto;

import com.banking.transaction_service.model.TransactionEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {

        private String fromAccountNumber;
        private String toAccountNumber;
        private BigDecimal amount;
        private String currency;
        private TransactionEnum.TransactionType type;
        private TransactionEnum.TransactionStatus status;
        private String description;
        private LocalDateTime createdAt;

}
