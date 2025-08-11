package com.banking.transaction_service;

import com.banking.transaction_service.model.Transaction;
import com.banking.transaction_service.model.TransactionEnum;
import com.banking.transaction_service.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest // מפעיל MongoDB משובץ לזמן הטסט
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void cleanUp() {
        transactionRepository.deleteAll();
    }

    @Test
    void testSaveAndFind() {
        Transaction tx = Transaction.builder()
                .transactionId("tx1")
                .fromAccountId("acc1")
                .toAccountId("acc2")
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .type(TransactionEnum.TransactionType.TRANSFER)
                .status(TransactionEnum.TransactionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(tx);

        List<Transaction> txs = transactionRepository.findByFromAccountIdOrToAccountId("acc1", "acc1");

        assertThat(txs).hasSize(1);
        assertThat(txs.get(0).getTransactionId()).isEqualTo("tx1");
    }

    @Test
    void testFindById() {
        Transaction tx = Transaction.builder()
                .transactionId("tx2")
                .amount(BigDecimal.valueOf(50))
                .status(TransactionEnum.TransactionStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(tx);

        var found = transactionRepository.findById("tx2");

        assertThat(found).isPresent();
        assertThat(found.get().getTransactionId()).isEqualTo("tx2");
    }
}
