package org.hposadas.reactivemongo.services;

import org.hposadas.reactivemongo.domain.Beer;
import org.hposadas.reactivemongo.mappers.BeerMapper;
import org.hposadas.reactivemongo.mappers.BeerMapperImpl;
import org.hposadas.reactivemongo.model.BeerDTO;
import org.hposadas.reactivemongo.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    BeerRepository beerRepository;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDto(getTestBeer());
    }

    @Test
    void testCreateBeer() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));

        savedMono.subscribe(beerDTO1 -> {
            System.out.println("Id del elemento creado : " + beerDTO1.getId());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);

    }

    @Test
    @DisplayName("Test Save Beer Using Subscriber")
    void name() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();

        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));

        savedMono.subscribe(savedBeerDto -> {
            System.out.println(savedBeerDto.getId());
            atomicBoolean.set(true);
            atomicDto.set(savedBeerDto);
        });

        await().untilTrue(atomicBoolean);
        BeerDTO persistedDto = atomicDto.get();

        assertThat(persistedDto).isNotNull();
        assertThat(persistedDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Save Beer Using Block")
    void testSaveBeerUsingBlock() {
        BeerDTO savedBeerDto = beerService.saveBeer(Mono.just(beerDTO)).block();
        assertThat(savedBeerDto).isNotNull();
        assertThat(savedBeerDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Update Beer Using Block")
    void testUpdateBlocking() {
        BeerDTO savedBeerDto = getSavedBeerDto();
        savedBeerDto.setBeerName("New Beer Name");

        BeerDTO updatedBeer = beerService.saveBeer(Mono.just(savedBeerDto)).block();
        BeerDTO fetchedDto = beerService.getById(updatedBeer.getId()).block();

        assertThat(fetchedDto.getBeerName()).isEqualToIgnoringCase("New Beer Name");
    }

    @Test
    @DisplayName("Test Update Using Reactive Streams")
    void testUpdateStreaming() {
        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();

        beerService.saveBeer(Mono.just(getTestBeerDto()))
                .map(savedBeerDto -> {
                    savedBeerDto.setBeerName("New Beer Name");
                    return savedBeerDto;
                }).flatMap(dtoModif -> beerService.saveBeer(dtoModif))
                .flatMap(savedUpdatedDto -> beerService.getById(savedUpdatedDto.getId()))
                .subscribe(dtoFromDb -> {
                    atomicDto.set(dtoFromDb);
                });

        await().until(() -> atomicDto.get() != null);

        assertThat(atomicDto.get().getBeerName()).isEqualTo("New Beer Name");
    }

    @Test
    void testDeleteBeer() {
        BeerDTO beerToDelete = getSavedBeerDto();

        beerService.deleteBeer(beerToDelete.getId()).block();

        Mono<BeerDTO> expectedEmptyMono = beerService.getById(beerToDelete.getId());
        BeerDTO emptyBeer = expectedEmptyMono.block();

        assertThat(emptyBeer).isNull();
    }

    @Test
    void findFirstByBeerNameTest() {
        BeerDTO beerSaved = getSavedBeerDto();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        Mono<BeerDTO> foundDto = beerService.findFirstByBeerName(beerSaved.getBeerName());

        foundDto.subscribe(dto -> {
            System.out.println(dto.toString());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void findByBeerStyleTest() {
        BeerDTO beerDTO1 = getSavedBeerDto();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        beerService.findByBeerStyle(beerDTO1.getBeerStyle())
                .subscribe(dto -> {
                    System.out.println(dto.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("123897")
                .build();
    }
    public BeerDTO getSavedBeerDto(){
        return beerService.saveBeer(Mono.just(getTestBeerDto())).block();
    }
    public static BeerDTO getTestBeerDto() {
        return new BeerMapperImpl().beerToBeerDto(getTestBeer());
    }

}