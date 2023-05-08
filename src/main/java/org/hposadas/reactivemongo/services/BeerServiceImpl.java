package org.hposadas.reactivemongo.services;

import lombok.RequiredArgsConstructor;
import org.hposadas.reactivemongo.domain.Beer;
import org.hposadas.reactivemongo.mappers.BeerMapper;
import org.hposadas.reactivemongo.model.BeerDTO;
import org.hposadas.reactivemongo.repositories.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll().map(beer -> beerMapper.beerToBeerDto(beer));
    }

    @Override
    public Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO) {
        return beerDTO.map(bDto -> beerMapper.beerDtoToBeer(bDto))
                .flatMap(beer -> beerRepository.save(beer))
                .map(beer -> beerMapper.beerToBeerDto(beer));
    }

    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
        return beerRepository.save(beerMapper.beerDtoToBeer(beerDTO))
                .map(beer -> beerMapper.beerToBeerDto(beer));
    }

    @Override
    public Mono<BeerDTO> getById(String beerId) {
        return beerRepository.findById(beerId)
                .map(beer -> beerMapper.beerToBeerDto(beer));
    }

    @Override
    public Mono<BeerDTO> updateBeer(String beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId)
                .map(beerToUpdate -> {
                    beerToUpdate.setBeerName(beerDTO.getBeerName());
                    beerToUpdate.setBeerStyle(beerDTO.getBeerStyle());
                    beerToUpdate.setPrice(beerDTO.getPrice());
                    beerToUpdate.setUpc(beerDTO.getUpc());
                    beerToUpdate.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    return beerToUpdate;
                })
                .flatMap(beer -> beerRepository.save(beer))
                .map(beer -> beerMapper.beerToBeerDto(beer));
    }

    @Override
    public Mono<BeerDTO> patchBeer(String beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId)
                .map(foundBeer -> {
                    if (StringUtils.hasText(beerDTO.getBeerName())){
                        foundBeer.setBeerName(beerDTO.getBeerName());
                    }
                    if (StringUtils.hasText(beerDTO.getBeerStyle())){
                        foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    }
                    if (beerDTO.getPrice() != null){
                        foundBeer.setPrice(beerDTO.getPrice());
                    }
                    if (StringUtils.hasText(beerDTO.getUpc())){
                        foundBeer.setUpc(beerDTO.getUpc());
                    }
                    if (beerDTO.getQuantityOnHand() != null) {
                        foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    }
                    return foundBeer;
                })
                .flatMap(beer -> beerRepository.save(beer))
                .map(beer -> beerMapper.beerToBeerDto(beer));
    }

    @Override
    public Flux<BeerDTO> findByBeerStyle(String beerStyle) {
        return beerRepository.findByBeerStyle(beerStyle)
                .map(beer -> beerMapper.beerToBeerDto(beer));
    }

    @Override
    public Mono<Void> deleteBeer(String beerId) {
        return beerRepository.deleteById(beerId);
    }

    @Override
    public Mono<BeerDTO> findFirstByBeerName(String beerName) {
        return beerRepository.findFirstByBeerName(beerName)
                .map(beer -> beerMapper.beerToBeerDto(beer));
    }
}