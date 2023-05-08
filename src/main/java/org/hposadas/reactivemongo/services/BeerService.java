package org.hposadas.reactivemongo.services;

import org.hposadas.reactivemongo.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {

    Flux<BeerDTO> listBeers();
    Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO);
    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);

    Mono<BeerDTO> getById(String beerId);
    Mono<BeerDTO> updateBeer(String beerId, BeerDTO beerDTO);
    Mono<BeerDTO> patchBeer(String beerId, BeerDTO beerDTO);
    Mono<Void> deleteBeer(String beerId);

    Mono<BeerDTO> findFirstByBeerName(String beerName);

    Flux<BeerDTO> findByBeerStyle(String beerStyle);


}
