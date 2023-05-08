package org.hposadas.reactivemongo.mappers;

import org.hposadas.reactivemongo.domain.Beer;
import org.hposadas.reactivemongo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO beerDTO);
    BeerDTO beerDtoToBeer(Beer beer);
}
