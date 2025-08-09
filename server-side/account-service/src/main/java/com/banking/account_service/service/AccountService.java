package com.banking.account_service.service;

import com.banking.account_service.dto.UpdateAccountDto;
import com.banking.account_service.exceptions.AccountServiceException;
import com.banking.account_service.model.Account;
import com.banking.account_service.repository.AccountRepository;
import com.banking.account_service.util.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.banking.account_service.util.ExceptionErrorConst.ACCOUNT_NOT_FOUND;
import static com.banking.account_service.util.ExceptionErrorConst.INSUFFICIENT;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountNumberCache accountNumberCache;

    public Account createAccount(Account account){
        String accountNumber=generateUniqueAccountNumber();
        account.setAccountNumber(accountNumber);
        return accountRepository.save(account);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = AccountNumberGenerator.createRandomAccountNumber();
        } while (accountNumberCache.exists(accountNumber));

        accountNumberCache.add(accountNumber);
        return accountNumber;
    }
    public Optional<Account> getAccountById(String accountId) {
        return accountRepository.findById(accountId);
    }

    public List<Account> getAccountsByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Account updateAccount(String accountNumber, UpdateAccountDto dto) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountServiceException(ACCOUNT_NOT_FOUND));

        if (dto.getOperationType() != null && dto.getAmount() != null) {
            BigDecimal currentBalance = account.getBalance();
            BigDecimal updatedBalance;

            switch (dto.getOperationType()) {
                case DEPOSIT:
                    updatedBalance = currentBalance.add(dto.getAmount());
                    break;
                case WITHDRAWAL:
                    if (currentBalance.compareTo(dto.getAmount()) < 0) {
                        throw new AccountServiceException(INSUFFICIENT);
                    }
                    updatedBalance = currentBalance.subtract(dto.getAmount());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation type");
            }

            account.setBalance(updatedBalance);
        }

        if (dto.getAccountType() != null) {
            account.setAccountType(dto.getAccountType());
        }
        if (dto.getCurrency() != null) {
            account.setCurrency(dto.getCurrency());
        }
        if (dto.getStatus() != null) {
            account.setStatus(dto.getStatus());
        }

        account.setUpdatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }


    public Optional<BigDecimal> getAccountBalance(String accountId) {
        return accountRepository.findById(accountId).map(Account::getBalance);
    }
}
