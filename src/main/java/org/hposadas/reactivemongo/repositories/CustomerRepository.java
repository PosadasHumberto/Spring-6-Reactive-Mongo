package org.hposadas.reactivemongo.repositories;

import org.hposadas.reactivemongo.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    public Mono<Customer> findFirstByCustomerName(String customerName);
}
