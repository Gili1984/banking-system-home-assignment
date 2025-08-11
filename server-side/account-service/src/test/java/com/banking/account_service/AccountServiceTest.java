package com.banking.account_service;

import com.banking.account_service.dto.UpdateAccountDto;
import com.banking.account_service.exceptions.AccountServiceException;
import com.banking.account_service.model.Account;
import com.banking.account_service.model.AccountEnums;
import com.banking.account_service.repository.AccountRepository;
import com.banking.account_service.service.AccountNumberCache;
import com.banking.account_service.service.AccountService;
import com.banking.account_service.util.AccountCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {


    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountNumberCache accountNumberCache;

    @Mock
    private AccountCache accountCache;

    @InjectMocks
    private AccountService accountService;

    @Test
    void testCreateAccount() {
        Account account = new Account();

        when(accountNumberCache.exists(anyString())).thenReturn(false);
        doNothing().when(accountNumberCache).add(anyString());

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account acc = invocation.getArgument(0);
            acc.setAccountId("generated-id-123");
            return acc;
        });

        Account created = accountService.createAccount(account);

        assertNotNull(created.getAccountNumber());
        assertEquals(BigDecimal.ZERO, created.getBalance());

        verify(accountCache).put(anyString(), anyString());
    }



    @Test
    void testUpdateAccount_deposit_success() {
        String accountNumber = "acc-123";
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId("id-1");

        UpdateAccountDto dto = new UpdateAccountDto();
        dto.setOperationType(AccountEnums.OperationType.DEPOSIT);
        dto.setAmount(BigDecimal.valueOf(50));

        when(accountCache.get(accountNumber)).thenReturn("id-1");
        when(accountRepository.findById("id-1")).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Account updated = accountService.updateAccount(accountNumber, dto);

        assertEquals(BigDecimal.valueOf(150), updated.getBalance());
    }

    @Test
    void testUpdateAccount_withdraw_insufficientBalance_throws() {
        String accountNumber = "acc-123";
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(100));
        account.setAccountId("id-1");

        UpdateAccountDto dto = new UpdateAccountDto();
        dto.setOperationType(AccountEnums.OperationType.WITHDRAWAL);
        dto.setAmount(BigDecimal.valueOf(150));

        lenient().when(accountCache.get(accountNumber)).thenReturn("id-1");
        lenient().when(accountRepository.findById("id-1")).thenReturn(Optional.of(account));

        assertThrows(AccountServiceException.class, () -> accountService.updateAccount(accountNumber, dto));
    }

}
