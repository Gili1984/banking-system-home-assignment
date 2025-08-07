package com.banking.account_service.repository;

import com.banking.account_service.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    List<Account> findByCustomerId(String customerId);
}
