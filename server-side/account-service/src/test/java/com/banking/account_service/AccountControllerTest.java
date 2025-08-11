package com.banking.account_service;

import com.banking.account_service.controller.AccountController;
import com.banking.account_service.model.Account;
import com.banking.account_service.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    void testGetAccountById_success() throws Exception {
        Account account = new Account();
        account.setAccountId("id-1");
        account.setAccountNumber("acc-001");

        when(accountService.getAccountById("id-1")).thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/v1/accounts/id-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("id-1"))
                .andExpect(jsonPath("$.accountNumber").value("acc-001"));
    }

    @Test
    void testGetAccountById_notFound() throws Exception {
        when(accountService.getAccountById("id-1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/accounts/id-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAccount_success() throws Exception {
        Account account = new Account();
        account.setAccountId("id-1");
        account.setAccountNumber("acc-001");

        when(accountService.createAccount(any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"cust1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("id-1"))
                .andExpect(jsonPath("$.accountNumber").value("acc-001"));
    }
}
