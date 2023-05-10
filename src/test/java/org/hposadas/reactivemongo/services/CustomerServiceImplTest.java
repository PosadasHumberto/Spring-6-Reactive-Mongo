package org.hposadas.reactivemongo.services;

import org.hposadas.reactivemongo.domain.Customer;
import org.hposadas.reactivemongo.mappers.BeerMapperImpl;
import org.hposadas.reactivemongo.mappers.CustomerMapper;
import org.hposadas.reactivemongo.mappers.CustomerMapperImpl;
import org.hposadas.reactivemongo.model.CustomerDTO;
import org.hposadas.reactivemongo.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceImplTest {
    
    @Autowired
    CustomerMapper customerMapper;
    
    @Autowired
    CustomerService customerService;
    
    @Autowired
    CustomerRepository customerRepository;
    
    CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = customerMapper.customerToCustomerDto(getTestCustomer());
    }

    @Test
    void testCreateCustomer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        Mono<CustomerDTO> savedMono = customerService.saveCustomer(Mono.just(customerDTO));
        savedMono.subscribe(customerDTO1 -> {
            System.out.println("Id coustumer : " + customerDTO1.getId());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    public static Customer getTestCustomer() {
        return Customer.builder()
                .customerName("Customer Name Test")
                .build();
    }

    public CustomerDTO getSavedCustomerDto() {
        return customerService.saveCustomer(Mono.just(getTestCustomerDto())).block();
    }

    public static CustomerDTO getTestCustomerDto() {
        return new CustomerMapperImpl().customerToCustomerDto(getTestCustomer());
    }
}