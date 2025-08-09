package com.banking.transaction_service.service;

import com.banking.transaction_service.dto.*;
import com.banking.transaction_service.exception.FromAccountServiceException;
import com.banking.transaction_service.model.Transaction;
import com.banking.transaction_service.model.TransactionEnum;
import com.banking.transaction_service.repository.TransactionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);


    private final String ACCOUNT_SERVICE_BASE_URL = "http://localhost:8081/api/v1/accounts";

    public Transaction deposit(DepositAndWithdrawDto request) {
        Transaction tx = Transaction.builder()
                .fromAccountId(null)
                .toAccountId(request.getAccountNumber()) // פה את שולחת accountNumber כ-id
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .status(TransactionEnum.TransactionStatus.PENDING)
                .type(TransactionEnum.TransactionType.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .build();

        tx = transactionRepository.save(tx);

        try {
            UpdateAccountDto updateDto = new UpdateAccountDto();
            updateDto.setOperationType(TransactionEnum.TransactionType.DEPOSIT);
            updateDto.setAmount(request.getAmount());

            String url = ACCOUNT_SERVICE_BASE_URL + "/" + request.getAccountNumber();

            HttpEntity<UpdateAccountDto> httpRequest = new HttpEntity<>(updateDto);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, httpRequest, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                tx.setStatus(TransactionEnum.TransactionStatus.FAILED);
                transactionRepository.save(tx);
                throw new FromAccountServiceException(
                        "Account update failed",
                        response.getStatusCodeValue(),
                        response.getBody()
                );            }

            tx.setStatus(TransactionEnum.TransactionStatus.COMPLETED);
            return transactionRepository.save(tx);

        } catch (RestClientException ex) {
            tx.setStatus(TransactionEnum.TransactionStatus.FAILED);
            transactionRepository.save(tx);
            throw new RuntimeException("Account service error: " + ex.getMessage(), ex);
        }
    }

    public Transaction withdraw(DepositAndWithdrawDto request) {
        Transaction tx = Transaction.builder()
                .fromAccountId(request.getAccountNumber()) // פה את שולחת accountNumber כ-id
                .toAccountId(null)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .status(TransactionEnum.TransactionStatus.PENDING)
                .type(TransactionEnum.TransactionType.WITHDRAWAL)
                .createdAt(LocalDateTime.now())
                .build();

        tx = transactionRepository.save(tx);

        try {
            UpdateAccountDto updateDto = new UpdateAccountDto();
            updateDto.setOperationType(TransactionEnum.TransactionType.WITHDRAWAL);
            updateDto.setAmount(request.getAmount());

            String url = ACCOUNT_SERVICE_BASE_URL + "/" + request.getAccountNumber();

            HttpEntity<UpdateAccountDto> httpRequest = new HttpEntity<>(updateDto);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, httpRequest, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                tx.setStatus(TransactionEnum.TransactionStatus.FAILED);
                transactionRepository.save(tx);
                throw new FromAccountServiceException(
                        "Account update failed",
                        response.getStatusCodeValue(),
                        response.getBody()
                );            }

            tx.setStatus(TransactionEnum.TransactionStatus.COMPLETED);
            return transactionRepository.save(tx);

        } catch (RestClientException ex) {
            tx.setStatus(TransactionEnum.TransactionStatus.FAILED);
            transactionRepository.save(tx);
            throw new RuntimeException("Account service error: " + ex.getMessage(), ex);
        }
    }
    public Transaction transfer(TransferRequestDto request) {
        Transaction tx = Transaction.builder()
                .fromAccountId(request.getFromAccountId())
                .toAccountId(request.getToAccountId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .status(TransactionEnum.TransactionStatus.PENDING)
                .type(TransactionEnum.TransactionType.TRANSFER)
                .createdAt(LocalDateTime.now())
                .build();

        tx = transactionRepository.save(tx);

        String fromUrl = ACCOUNT_SERVICE_BASE_URL + "/" + request.getFromAccountId();
        String toUrl = ACCOUNT_SERVICE_BASE_URL + "/" + request.getToAccountId();

        try {
            UpdateAccountDto withdrawDto = new UpdateAccountDto();
            withdrawDto.setOperationType(TransactionEnum.TransactionType.WITHDRAWAL);
            withdrawDto.setAmount(request.getAmount());

            HttpEntity<UpdateAccountDto> withdrawRequest = new HttpEntity<>(withdrawDto);
            ResponseEntity<String> withdrawResponse = restTemplate.exchange(
                    fromUrl, HttpMethod.PUT, withdrawRequest, String.class
            );

            if (!withdrawResponse.getStatusCode().is2xxSuccessful()) {
                tx.setStatus(TransactionEnum.TransactionStatus.FAILED);
                transactionRepository.save(tx);
                throw new FromAccountServiceException(
                        "From account update failed",
                        withdrawResponse.getStatusCodeValue(),
                        withdrawResponse.getBody()
                );
            }

            UpdateAccountDto depositDto = new UpdateAccountDto();
            depositDto.setOperationType(TransactionEnum.TransactionType.DEPOSIT);
            depositDto.setAmount(request.getAmount());

            HttpEntity<UpdateAccountDto> depositRequest = new HttpEntity<>(depositDto);
            ResponseEntity<String> depositResponse = restTemplate.exchange(
                    toUrl, HttpMethod.PUT, depositRequest, String.class
            );

            if (!depositResponse.getStatusCode().is2xxSuccessful()) {
                boolean rollbackSucceeded = false;
                try {
                    rollbackFromAccount(fromUrl, request.getAmount());
                    rollbackSucceeded = true;
                } catch (Exception rollbackEx) {
                    log.error("Rollback failed for transaction {}: {}", tx.getTransactionId(), rollbackEx.getMessage(), rollbackEx);
                }

                tx.setStatus(TransactionEnum.TransactionStatus.FAILED);
                transactionRepository.save(tx);

                if (rollbackSucceeded) {
                    log.info("Transaction {} marked FAILED after successful rollback", tx.getTransactionId());
                } else {
                    log.error("Transaction {} marked FAILED but rollback failed — manual intervention required!", tx.getTransactionId());
                }

                throw new FromAccountServiceException(
                        "To account update failed",
                        depositResponse.getStatusCodeValue(),
                        depositResponse.getBody()
                );
            }

            tx.setStatus(TransactionEnum.TransactionStatus.COMPLETED);
            return transactionRepository.save(tx);

        } catch (RestClientException ex) {
            tx.setStatus(TransactionEnum.TransactionStatus.FAILED);
            transactionRepository.save(tx);
            throw new RuntimeException("Account service error: " + ex.getMessage(), ex);
        }
    }


    private void rollbackFromAccount(String fromUrl, BigDecimal amount) {
        try {
            UpdateAccountDto rollbackDto = new UpdateAccountDto();
            rollbackDto.setOperationType(TransactionEnum.TransactionType.DEPOSIT);
            rollbackDto.setAmount(amount);

            HttpEntity<UpdateAccountDto> rollbackRequest = new HttpEntity<>(rollbackDto);
            restTemplate.exchange(fromUrl, HttpMethod.PUT, rollbackRequest, String.class);
        } catch (RestClientException ex) {
            System.err.println("Rollback failed: " + ex.getMessage());
        }
    }


    public List<Transaction> getTransactionsForAccount(String accountId) {
        return transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId);
    }

    public Optional<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }
}
