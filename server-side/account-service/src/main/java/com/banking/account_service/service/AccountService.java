package com.banking.account_service.service;

import com.banking.account_service.model.Account;
import com.banking.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountById(String accountId) {
        return accountRepository.findById(accountId);
    }

    public List<Account> getAccountsByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Optional<Account> updateAccount(String accountId, Account updatedAccount) {
        return accountRepository.findById(accountId).map(existing -> {
            existing.setAccountType(updatedAccount.getAccountType());
            existing.setStatus(updatedAccount.getStatus());
            existing.setCurrency(updatedAccount.getCurrency());
            existing.setBalance(updatedAccount.getBalance());
            existing.setUpdatedAt(LocalDateTime.now());
            return accountRepository.save(existing);
        });
    }

    public Optional<BigDecimal> getAccountBalance(String accountId) {
        return accountRepository.findById(accountId).map(Account::getBalance);
    }
}
