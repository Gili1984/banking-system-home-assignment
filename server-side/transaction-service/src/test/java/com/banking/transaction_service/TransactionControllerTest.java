package com.banking.transaction_service;

import com.banking.transaction_service.controller.TransactionController;
import com.banking.transaction_service.dto.DepositAndWithdrawDto;
import com.banking.transaction_service.dto.TransferRequestDto;
import com.banking.transaction_service.model.Transaction;
import com.banking.transaction_service.model.TransactionEnum;
import com.banking.transaction_service.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction sampleTransaction;

    @BeforeEach
    void setup() {
        sampleTransaction = Transaction.builder()
                .transactionId("tx123")
                .fromAccountId("acc1")
                .toAccountId("acc2")
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .type(TransactionEnum.TransactionType.TRANSFER)
                .status(TransactionEnum.TransactionStatus.COMPLETED)
                .description("Test transfer")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testDepositEndpoint() throws Exception {
        DepositAndWithdrawDto dto = new DepositAndWithdrawDto();
        dto.setAccountNumber("acc1");
        dto.setAmount(BigDecimal.valueOf(50));
        dto.setCurrency("USD");
        dto.setDescription("Deposit test");

        Mockito.when(transactionService.deposit(any(DepositAndWithdrawDto.class)))
                .thenReturn(sampleTransaction);

        mockMvc.perform(post("/api/v2/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx123"))
                .andExpect(jsonPath("$.type").value("TRANSFER"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testWithdrawEndpoint() throws Exception {
        DepositAndWithdrawDto dto = new DepositAndWithdrawDto();
        dto.setAccountNumber("acc1");
        dto.setAmount(BigDecimal.valueOf(30));
        dto.setCurrency("USD");
        dto.setDescription("Withdraw test");

        Mockito.when(transactionService.withdraw(any(DepositAndWithdrawDto.class)))
                .thenReturn(sampleTransaction);

        mockMvc.perform(post("/api/v2/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx123"));
    }

    @Test
    void testTransferEndpoint() throws Exception {
        TransferRequestDto dto = new TransferRequestDto();
        dto.setFromAccountNumber("acc1");
        dto.setToAccountNumber("acc2");
        dto.setAmount(BigDecimal.valueOf(20));
        dto.setCurrency("USD");
        dto.setDescription("Transfer test");

        Mockito.when(transactionService.transfer(any(TransferRequestDto.class)))
                .thenReturn(sampleTransaction);

        mockMvc.perform(post("/api/v2/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx123"));
    }

    @Test
    void testGetTransactionsByAccount() throws Exception {
        Mockito.when(transactionService.getTransactionsForAccount(eq("acc1")))
                .thenReturn(List.of(sampleTransaction));

        mockMvc.perform(get("/api/v2/transactions/account/acc1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value("tx123"));
    }

    @Test
    void testGetTransactionById_found() throws Exception {
        Mockito.when(transactionService.getTransactionById(eq("tx123")))
                .thenReturn(Optional.of(sampleTransaction));

        mockMvc.perform(get("/api/v2/transactions/tx123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx123"));
    }

    @Test
    void testGetTransactionById_notFound() throws Exception {
        Mockito.when(transactionService.getTransactionById(eq("notfound")))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v2/transactions/notfound"))
                .andExpect(status().isNotFound());
    }
}
