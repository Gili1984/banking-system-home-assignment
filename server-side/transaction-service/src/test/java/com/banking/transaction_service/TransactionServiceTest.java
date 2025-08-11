package com.banking.transaction_service;

import com.banking.transaction_service.dto.DepositAndWithdrawDto;
import com.banking.transaction_service.dto.TransferRequestDto;
import com.banking.transaction_service.model.Transaction;
import com.banking.transaction_service.model.TransactionEnum;
import com.banking.transaction_service.repository.TransactionRepository;
import com.banking.transaction_service.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    TransactionService transactionService;

    public TransactionServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeposit_success() {
        DepositAndWithdrawDto request = new DepositAndWithdrawDto();
        request.setAccountNumber("acc123");
        request.setAmount(BigDecimal.TEN);
        request.setCurrency("USD");
        request.setDescription("deposit");

        Transaction savedTx = Transaction.builder()
                .transactionId("tx1")
                .status(TransactionEnum.TransactionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(transactionRepository.save(any())).thenReturn(savedTx);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("OK", HttpStatus.OK));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0)); // for status update save

        Transaction result = transactionService.deposit(request);

        assertThat(result.getStatus()).isEqualTo(TransactionEnum.TransactionStatus.COMPLETED);
        verify(transactionRepository, times(2)).save(any());
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testWithdraw_failure_rollsBackAndThrows() {
        DepositAndWithdrawDto request = new DepositAndWithdrawDto();
        request.setAccountNumber("acc123");
        request.setAmount(BigDecimal.TEN);
        request.setCurrency("USD");
        request.setDescription("withdraw");

        Transaction savedTx = Transaction.builder()
                .transactionId("tx2")
                .status(TransactionEnum.TransactionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(transactionRepository.save(any())).thenReturn(savedTx);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Account service error"));

        assertThatThrownBy(() -> transactionService.withdraw(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account service error");

        verify(transactionRepository, times(2)).save(any());
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testTransfer_success() {
        TransferRequestDto request = new TransferRequestDto();
        request.setFromAccountNumber("accFrom");
        request.setToAccountNumber("accTo");
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("USD");
        request.setDescription("transfer");

        Transaction savedTx = Transaction.builder()
                .transactionId("tx3")
                .status(TransactionEnum.TransactionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(transactionRepository.save(any())).thenReturn(savedTx);
        when(restTemplate.exchange(contains("accFrom"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("OK", HttpStatus.OK));
        when(restTemplate.exchange(contains("accTo"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("OK", HttpStatus.OK));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Transaction result = transactionService.transfer(request);

        assertThat(result.getStatus()).isEqualTo(TransactionEnum.TransactionStatus.COMPLETED);
        verify(restTemplate, times(2)).exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class));
        verify(transactionRepository, times(2)).save(any());
    }

    @Test
    void testGetTransactionsForAccount() {
        String accId = "acc123";

        Transaction tx1 = Transaction.builder().transactionId("tx1").fromAccountId(accId).build();
        Transaction tx2 = Transaction.builder().transactionId("tx2").toAccountId(accId).build();

        when(transactionRepository.findByFromAccountIdOrToAccountId(accId, accId)).thenReturn(List.of(tx1, tx2));

        List<Transaction> list = transactionService.getTransactionsForAccount(accId);

        assertThat(list).hasSize(2);
        verify(transactionRepository).findByFromAccountIdOrToAccountId(accId, accId);
    }

    @Test
    void testGetTransactionById_found() {
        Transaction tx = Transaction.builder().transactionId("tx1").build();
        when(transactionRepository.findById("tx1")).thenReturn(Optional.of(tx));

        Optional<Transaction> opt = transactionService.getTransactionById("tx1");

        assertThat(opt).isPresent();
        assertThat(opt.get().getTransactionId()).isEqualTo("tx1");
        verify(transactionRepository).findById("tx1");
    }

    @Test
    void testGetTransactionById_notFound() {
        when(transactionRepository.findById("nonexistent")).thenReturn(Optional.empty());

        Optional<Transaction> opt = transactionService.getTransactionById("nonexistent");

        assertThat(opt).isEmpty();
        verify(transactionRepository).findById("nonexistent");
    }
}
