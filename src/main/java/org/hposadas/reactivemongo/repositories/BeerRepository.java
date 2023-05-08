package org.hposadas.reactivemongo.repositories;

import org.hposadas.reactivemongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {

}
