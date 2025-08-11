package com.banking.account_service;

import com.banking.account_service.model.Account;
import com.banking.account_service.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void cleanDatabase() {
        accountRepository.deleteAll();
    }

    @Test
    void testFindByCustomerId() {
        Account acc = new Account();
        acc.setCustomerId("customer-1");
        acc.setAccountNumber("acc-001");
        acc.setBalance(BigDecimal.valueOf(100));
        accountRepository.save(acc);

        List<Account> accounts = accountRepository.findByCustomerId("customer-1");

        assertThat(accounts).isNotEmpty();
        assertThat(accounts.get(0).getCustomerId()).isEqualTo("customer-1");
    }

    @Test
    void testFindByAccountNumber() {
        Account acc = new Account();
        acc.setAccountNumber("acc-002");
        acc.setBalance(BigDecimal.valueOf(50));
        accountRepository.save(acc);

        Optional<Account> accountOpt = accountRepository.findByAccountNumber("acc-002");

        assertThat(accountOpt).isPresent();
        assertThat(accountOpt.get().getAccountNumber()).isEqualTo("acc-002");
    }
}

