package org.hposadas.reactivemongo.repositories;

import org.hposadas.reactivemongo.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

}
