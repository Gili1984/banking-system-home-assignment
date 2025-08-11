package com.banking.transaction_service.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @JsonIgnore
    @Id
    private String transactionId;
    @JsonIgnore
    private String fromAccountId;
    @JsonIgnore
    private String toAccountId;
    private BigDecimal amount;
    private String currency;
    private TransactionEnum.TransactionType type;
    private TransactionEnum.TransactionStatus status;
    private String description;
    @CreatedDate
    private LocalDateTime createdAt;

}
