package com.banking.transaction_service.repository;

import com.banking.transaction_service.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByFromAccountIdOrToAccountId(String fromAccountId, String toAccountId);

}
