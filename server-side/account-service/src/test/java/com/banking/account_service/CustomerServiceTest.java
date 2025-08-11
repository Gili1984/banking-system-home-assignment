package com.banking.account_service;

import com.banking.account_service.model.Customer;
import com.banking.account_service.repository.CustomerRepository;
import com.banking.account_service.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Alice");

        when(customerRepository.save(customer)).thenReturn(customer);

        Customer created = customerService.createCustomer(customer);

        assertThat(created.getFirstName()).isEqualTo("Alice");
        verify(customerRepository).save(customer);
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = List.of(new Customer(), new Customer());

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertThat(result).hasSize(2);
        verify(customerRepository).findAll();
    }

    @Test
    void testGetCustomerById_found() {
        Customer customer = new Customer();
        customer.setFirstName("Bob");

        when(customerRepository.findById("123")).thenReturn(Optional.of(customer));

        Optional<Customer> found = customerService.getCustomerById("123");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Bob");
        verify(customerRepository).findById("123");
    }

    @Test
    void testGetCustomerById_notFound() {
        when(customerRepository.findById("123")).thenReturn(Optional.empty());

        Optional<Customer> found = customerService.getCustomerById("123");

        assertThat(found).isNotPresent();
        verify(customerRepository).findById("123");
    }
}
