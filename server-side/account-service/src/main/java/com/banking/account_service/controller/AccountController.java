package com.banking.account_service.controller;
import com.banking.account_service.dto.UpdateAccountDto;
import com.banking.account_service.model.Account;
import com.banking.account_service.service.AccountService;
import com.banking.account_service.util.AccountCache;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountCache accountCache;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountId) {
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Account>> getAccountsByCustomer(@PathVariable String customerId) {
        List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountNumber, @Valid @RequestBody UpdateAccountDto dto) {
        Account updatedAccount = accountService.updateAccount(accountNumber, dto);
        return ResponseEntity.ok(updatedAccount);
    }


    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountId) {
        return accountService.getAccountBalance(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getIdByAccountNumber/{accountNumber}")
    public ResponseEntity<String> getAccountIdByAccountNumber(@PathVariable String accountNumber) {
        String accountId = accountCache.get(accountNumber);
        if (accountId != null) {
            return ResponseEntity.ok(accountId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
