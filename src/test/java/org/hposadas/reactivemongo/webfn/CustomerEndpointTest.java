package org.hposadas.reactivemongo.webfn;

import org.hposadas.reactivemongo.domain.Customer;
import org.hposadas.reactivemongo.model.CustomerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    public CustomerDTO getsavedTestCustomer() {
        return webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .returnResult(CustomerDTO.class)
                .getResponseBody().blockFirst();
    }


    @Test
    void listCustomersTest() {
        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }

    @Test
    void getCustomerByIdTest() {
        CustomerDTO customerDTO = getsavedTestCustomer();

        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    void testCreateNewCustomer() {
        CustomerDTO customerToSave = CustomerDTO.builder()
                .customerName("Test to Create Customer")
                .build();

        webTestClient.post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(customerToSave), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectHeader().exists("Location")
                .expectStatus().isCreated();
    }

    @Test
    void testUpdateCustomerById() {
        CustomerDTO customerDTO = getsavedTestCustomer();
        customerDTO.setCustomerName("Updated Customer Name");

        webTestClient.put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testPatchCustomerById() {
        CustomerDTO customerDTO = getsavedTestCustomer();
        customerDTO.setCustomerName("Modified Name To Patch");

        webTestClient.patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteCustomerById() {
        CustomerDTO customerDTO = getsavedTestCustomer();

        webTestClient.delete().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testPatchIdNotFound() {
        webTestClient.patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getCustomerDto()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteNotFound() {
        webTestClient.delete().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateCustomerBadRequest() {
        CustomerDTO customerDTO = getsavedTestCustomer();
        customerDTO.setCustomerName("a");

        webTestClient.put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateCustomerNotFound() {
        webTestClient.put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getCustomerDto()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetByIdNotFound() {
        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    public static CustomerDTO getCustomerDto() {
        return CustomerDTO.builder()
                .customerName("Test Customer")
                .build();
    }
}