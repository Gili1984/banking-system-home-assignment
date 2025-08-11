package com.banking.transaction_service.service;

import com.banking.transaction_service.dto.*;
import com.banking.transaction_service.exception.FromAccountServiceException;
import com.banking.transaction_service.model.Transaction;
import com.banking.transaction_service.model.TransactionEnum;
import com.banking.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
                .fromAccountId(getAccountIdFromAccountNumber(request.getAccountNumber()))
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .status(TransactionEnum.TransactionStatus.PENDING)
                .type(TransactionEnum.TransactionType.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .build();

        tx = transactionRepository.save(tx);

        String url = ACCOUNT_SERVICE_BASE_URL + "/" + request.getAccountNumber();

        try {
            executeAccountUpdate(
                    url,
                    request.getAmount(),
                    TransactionEnum.TransactionType.DEPOSIT,
                    tx,
                    false
            );

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
                .fromAccountId(getAccountIdFromAccountNumber(request.getAccountNumber()))
                .toAccountId(null)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .status(TransactionEnum.TransactionStatus.PENDING)
                .type(TransactionEnum.TransactionType.WITHDRAWAL)
                .createdAt(LocalDateTime.now())
                .build();

        tx = transactionRepository.save(tx);

        String url = ACCOUNT_SERVICE_BASE_URL + "/" + request.getAccountNumber();

        try {
            executeAccountUpdate(
                    url,
                    request.getAmount(),
                    TransactionEnum.TransactionType.WITHDRAWAL,
                    tx,
                    false
            );

            tx.setStatus(TransactionEnum.TransactionStatus.COMPLETED);
            return transactionRepository.save(tx);

        } catch (Exception ex) {
            tx.setStatus(TransactionEnum.TransactionStatus.FAILED);
            transactionRepository.save(tx);
            throw new RuntimeException("Account service error: " + ex.getMessage(), ex);
        }
    }


    public Transaction transfer(TransferRequestDto request) {

        Transaction tx = Transaction.builder()
                .fromAccountId(getAccountIdFromAccountNumber(request.getFromAccountNumber()))
                .toAccountId(getAccountIdFromAccountNumber(request.getToAccountNumber()))
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .status(TransactionEnum.TransactionStatus.PENDING)
                .type(TransactionEnum.TransactionType.TRANSFER)
                .createdAt(LocalDateTime.now())
                .build();

        tx = transactionRepository.save(tx);

        String fromUrl = ACCOUNT_SERVICE_BASE_URL + "/" + request.getFromAccountNumber();
        String toUrl = ACCOUNT_SERVICE_BASE_URL + "/" + request.getToAccountNumber();

        try {
            executeAccountUpdate(fromUrl, request.getAmount(), TransactionEnum.TransactionType.WITHDRAWAL, tx, false);
            try {
                executeAccountUpdate(toUrl, request.getAmount(), TransactionEnum.TransactionType.DEPOSIT, tx, true, fromUrl);
            } catch (FromAccountServiceException ex) {
                throw ex;
            }
            tx.setStatus(TransactionEnum.TransactionStatus.COMPLETED);
            return transactionRepository.save(tx);

        } catch (RestClientException ex) {
            tx.setStatus(TransactionEnum.TransactionStatus.FAILED);
            transactionRepository.save(tx);
            throw new RuntimeException("Account service error: " + ex.getMessage(), ex);
        }
    }
    private String getAccountIdFromAccountNumber(String accountNumber) {
        String url = ACCOUNT_SERVICE_BASE_URL +"/getIdByAccountNumber/"+ accountNumber;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            else {
                    String errorMsg = "Failed to get accountId: received non-success status or empty body from account service";
                    log.error(errorMsg + " for accountNumber: {}", accountNumber);
                    throw new FromAccountServiceException(errorMsg, response.getStatusCodeValue(), "Empty or invalid response");

            }
        } catch (RestClientException ex) {
            log.error("Error calling account service for accountNumber {}: {}", accountNumber, ex.getMessage(), ex);
            throw new FromAccountServiceException("Account service unreachable or returned error: " + ex.getMessage(), 500, ex.getMessage());
        }
    }

    /**
     * func for update account
     * @param url url for account service path
     * @param amount the amount fot update
     * @param type deposit/withdraw
     * @param tx the transaction object build
     * @param rollbackOnFailure  do make rollback(boolean)
     * @param rollbackUrl url for rollback
     */
    private void executeAccountUpdate(String url, BigDecimal amount, TransactionEnum.TransactionType type, Transaction tx, boolean rollbackOnFailure, String... rollbackUrl) {
        UpdateAccountDto dto = new UpdateAccountDto();
        dto.setOperationType(type);
        dto.setAmount(amount);

        HttpEntity<UpdateAccountDto> request = new HttpEntity<>(dto);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                handleFailure(tx, rollbackOnFailure,
                        rollbackUrl.length > 0 ? rollbackUrl[0] : null,
                        amount,
                        response.getBody(),
                        response.getStatusCodeValue(),
                        "Account update failed");            }
        } catch (HttpClientErrorException ex) {
            handleFailure(tx, rollbackOnFailure,
                    rollbackUrl.length > 0 ? rollbackUrl[0] : null,
                    amount,
                    ex.getResponseBodyAsString(),
                    ex.getStatusCode().value(),
                    "Account update failed");     }
    }

    private void handleFailure(Transaction tx, boolean rollbackOnFailure, String rollbackUrl,
                               BigDecimal amount, String errorBody, int statusCode, String failureMessage) {
        boolean rollbackSucceeded = false;

        if (rollbackOnFailure && rollbackUrl != null) {
            try {
                rollbackFromAccount(rollbackUrl, amount);
                rollbackSucceeded = true;
            } catch (Exception rollbackEx) {
                log.error("Rollback failed for transaction {}: {}", tx.getTransactionId(), rollbackEx.getMessage(), rollbackEx);
            }
        }

        tx.setStatus(TransactionEnum.TransactionStatus.FAILED);
        transactionRepository.save(tx);

        if (rollbackOnFailure) {
            if (rollbackSucceeded) {
                log.info("Transaction {} marked FAILED after successful rollback", tx.getTransactionId());
            } else {
                log.error("Transaction {} marked FAILED but rollback failed — manual intervention required!", tx.getTransactionId());
            }
        }

        throw new FromAccountServiceException(failureMessage, statusCode, errorBody);
    }




    private void rollbackFromAccount(String fromUrl, BigDecimal amount) {
        UpdateAccountDto rollbackDto = new UpdateAccountDto();
        rollbackDto.setOperationType(TransactionEnum.TransactionType.DEPOSIT);
        rollbackDto.setAmount(amount);

        HttpEntity<UpdateAccountDto> rollbackRequest = new HttpEntity<>(rollbackDto);
        restTemplate.exchange(fromUrl, HttpMethod.PUT, rollbackRequest, String.class);
    }


    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        String accountId=getAccountIdFromAccountNumber(accountNumber);
        return transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId);
    }

    public Optional<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }
}
