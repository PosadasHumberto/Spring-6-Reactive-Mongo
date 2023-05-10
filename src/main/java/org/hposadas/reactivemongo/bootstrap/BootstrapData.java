package org.hposadas.reactivemongo.bootstrap;

import lombok.RequiredArgsConstructor;
import org.hposadas.reactivemongo.domain.Beer;
import org.hposadas.reactivemongo.domain.Customer;
import org.hposadas.reactivemongo.repositories.BeerRepository;
import org.hposadas.reactivemongo.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        beerRepository.deleteAll()
                .doOnSuccess(success -> {
                    loadBeerData();
                }).subscribe();
        customerRepository.deleteAll()
                .doOnSuccess(success -> loadCustomerData()).subscribe();
    }

    private void loadCustomerData() {
        customerRepository.count().subscribe(count -> {
            if (count==0) {
                Customer c1 = Customer.builder()
                        .customerName("Customer 1")
                        .build();

                Customer c2 = Customer.builder()
                        .customerName("Customer 2")
                        .build();

                Customer c3 = Customer.builder()
                        .customerName("Customer 3")
                        .build();

                customerRepository.save(c1).subscribe();
                customerRepository.save(c2).subscribe();
                customerRepository.save(c3).subscribe();
            }
        });
    }

    private void loadBeerData() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                        .beerName("Galaxy Cat")
                        .beerStyle("Pale Ale")
                        .upc("12356")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(122)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer2 = Beer.builder()
                        .beerName("Crank")
                        .beerStyle("Pale Ale")
                        .upc("12356222")
                        .price(new BigDecimal("11.99"))
                        .quantityOnHand(392)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer3 = Beer.builder()
                        .beerName("Sunshine City")
                        .beerStyle("IPA")
                        .upc("12356")
                        .price(new BigDecimal("13.99"))
                        .quantityOnHand(144)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                beerRepository.save(beer1).subscribe();
                beerRepository.save(beer2).subscribe();
                beerRepository.save(beer3).subscribe();
            }
    });
}
}
