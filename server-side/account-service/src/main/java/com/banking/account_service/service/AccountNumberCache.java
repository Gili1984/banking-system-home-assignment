package com.banking.account_service.service;

import com.banking.account_service.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AccountNumberCache {
    private final AccountRepository accountRepository;

    private Set<String> accountNumbers = ConcurrentHashMap.newKeySet();

    public boolean exists(String accountNumber) {
        return accountNumbers.contains(accountNumber);
    }

    public void add(String accountNumber) {
        accountNumbers.add(accountNumber);
    }

    public void clear() {
        accountNumbers.clear();
    }

    @PostConstruct
    public void loadAccountNumbers() {
        clear();
        List<String> allAccountNumbers = accountRepository.findAllAccountNumbers();
        accountNumbers.addAll(allAccountNumbers);
    }
}
