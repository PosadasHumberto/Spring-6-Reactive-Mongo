package org.hposadas.reactivemongo.services;

import lombok.RequiredArgsConstructor;
import org.hposadas.reactivemongo.mappers.BeerMapper;
import org.hposadas.reactivemongo.model.BeerDTO;
import org.hposadas.reactivemongo.repositories.BeerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;

    @Override
    public Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO) {
        return beerDTO.map(bDto -> beerMapper.beerDtoToBeer(bDto))
                .flatMap(beer -> beerRepository.save(beer))
                .map(beer -> beerMapper.beerToBeerDto(beer));
    }

    @Override
    public Mono<BeerDTO> getById(String id) {
        return null;
    }
}
