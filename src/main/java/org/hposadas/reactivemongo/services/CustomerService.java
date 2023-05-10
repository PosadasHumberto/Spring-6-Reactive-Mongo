package org.hposadas.reactivemongo.services;

import org.hposadas.reactivemongo.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Flux<CustomerDTO> listCustomers();
    Mono<CustomerDTO> getCustomerById(String customerId);
    Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO);
    Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO);
    Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO);
    Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO);
    Mono<Void> deleteCustomer(String customerId);

    Mono<CustomerDTO> findFirstByCustomerName(String customerName);

}
