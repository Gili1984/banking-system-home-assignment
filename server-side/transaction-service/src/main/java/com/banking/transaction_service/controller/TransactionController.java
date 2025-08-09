package com.banking.transaction_service.controller;

import com.banking.transaction_service.dto.*;
import com.banking.transaction_service.exception.FromAccountServiceException;
import com.banking.transaction_service.model.Transaction;
import com.banking.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/v2/transactions")
@RequiredArgsConstructor
@CrossOrigin
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public String deposit(@RequestBody DepositAndWithdrawDto request) {
        try {
            Transaction transaction = transactionService.deposit(request);
            return "ההפקדה בוצעה בהצלחה";
        } catch (FromAccountServiceException ex) {
            if ("2".equals(ex.getErrorNumber())) {
                return "חשבון לא קיים במערכת";
            }
        } catch (Exception ex) {
            return "ההפקדה לא בוצע- שגיאת רשת";
        }
        return "";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody DepositAndWithdrawDto request) {
        try {
            Transaction transaction = transactionService.withdraw(request);
            return "ההפקדה בוצעה בהצלחה";
        } catch (FromAccountServiceException ex) {
            if ("2".equals(ex.getErrorNumber())) {
                return "חשבון לא קיים במערכת";
            } else if ("3".equals(ex.getErrorNumber())) {
                return "אין יתרה בחשבונך";
            }
        } catch (Exception ex) {
            return "ההפקדה לא בוצע- שגיאת רשת";
        }
        return "";
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferRequestDto request) {
        return ResponseEntity.ok(transactionService.transfer(request));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@PathVariable String accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsForAccount(accountId));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String transactionId) {
        return transactionService.getTransactionById(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
