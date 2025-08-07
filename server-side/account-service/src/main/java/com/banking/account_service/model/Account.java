package com.banking.account_service.model;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    private String accountId;

    private String customerId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum AccountType {
        SAVINGS,
        CHECKING
    }

    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        BLOCKED
    }
}
