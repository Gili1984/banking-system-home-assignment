package com.banking.account_service;

import com.banking.account_service.repository.AccountRepository;
import com.banking.account_service.service.AccountNumberCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountNumberCacheTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountNumberCache accountNumberCache;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountNumberCache = new AccountNumberCache(accountRepository);
    }

    @Test
    void testLoadAccountNumbers_loadsFromRepository() {
        List<String> accountNumbersFromDb = List.of("acc-001", "acc-002");
        when(accountRepository.findAllAccountNumbers()).thenReturn(accountNumbersFromDb);

        accountNumberCache.clear();
        accountNumberCache.loadAccountNumbers();

        assertTrue(accountNumberCache.exists("acc-001"));
        assertTrue(accountNumberCache.exists("acc-002"));
        assertFalse(accountNumberCache.exists("acc-003"));
    }

    @Test
    void testAddAndExists() {
        accountNumberCache.clear();
        accountNumberCache.add("acc-123");

        assertTrue(accountNumberCache.exists("acc-123"));
        assertFalse(accountNumberCache.exists("acc-999"));
    }
}
