package com.banking.account_service.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AccountCache {
    private final Cache<String, String> accountNumberToIdCache;

    public AccountCache() {
        accountNumberToIdCache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.DAYS)
                .maximumSize(10_000)
                .build();
    }

    public void put(String accountNumber, String accountId) {
        if (accountNumber != null && accountId != null) {
            accountNumberToIdCache.put(accountNumber, accountId);
        } else {
            System.out.println("AccountCache.put called with null value: accountNumber=" + accountNumber + ", accountId=" + accountId);
        }
    }


    public String get(String accountNumber) {
        return accountNumberToIdCache.getIfPresent(accountNumber);
    }

    public void remove(String accountNumber) {
        accountNumberToIdCache.invalidate(accountNumber);
    }
}
