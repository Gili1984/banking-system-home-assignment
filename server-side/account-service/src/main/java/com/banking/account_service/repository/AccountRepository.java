package com.banking.account_service.repository;

import com.banking.account_service.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    List<Account> findByCustomerId(String customerId);
    Optional<Account> findByAccountNumber(String accountNumber);
    @Query(value = "{}", fields = "{accountNumber : 1, accountId: 0}")
    List<String> findAllAccountNumbers();
}
