package com.banking.account_service.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class AccountCache {
    public final Cache<String, String> accountNumberToIdCache;
    private static final Logger log = LoggerFactory.getLogger(AccountCache.class);


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
            log.info("AccountCache.put called with null value: accountNumber={}, accountId={}", accountNumber, accountId);
        }
    }


    public String get(String accountNumber) {
        return accountNumberToIdCache.getIfPresent(accountNumber);
    }

    public void remove(String accountNumber) {
        accountNumberToIdCache.invalidate(accountNumber);
    }

}
