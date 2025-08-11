package com.banking.account_service;

import com.banking.account_service.model.Customer;
import com.banking.account_service.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveAndFindById() {
        Customer customer = new Customer();
        customer.setFirstName("John Doe");
        customer = customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findById(customer.getCustomerId());

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John Doe");
    }
}
