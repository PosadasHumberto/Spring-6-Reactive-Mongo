package org.hposadas.reactivemongo.services;

import org.hposadas.reactivemongo.model.BeerDTO;
import reactor.core.publisher.Mono;

public interface BeerService {

    Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO);

    Mono<BeerDTO> getById(String id);
}
