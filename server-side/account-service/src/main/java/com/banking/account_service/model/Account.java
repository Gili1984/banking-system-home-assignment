package com.banking.account_service.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @JsonIgnore
    @Id
    private String accountId;
    private String customerId;
    @Indexed(unique = true)
    private String accountNumber;
    private AccountEnums.AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private AccountEnums.AccountStatus status;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
