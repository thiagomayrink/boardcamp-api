package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.repositories.CustomerRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CustomerServiceIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    @AfterEach
    void cleanUpDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    void givenRepeatedCustomerCpf_whenCreatingCustomer_thenThrowsError() {
        // given
        CustomerModel customerConflict = new CustomerModel(1L, "Customer name", "01234567890");
        customerRepository.save(customerConflict);

        CustomerDTO customerDto = new CustomerDTO("Customer name", "01234567890");
        HttpEntity<CustomerDTO> body = new HttpEntity<>(customerDto);

        // when
        ResponseEntity<String> response =
                restTemplate.exchange("/api/customers", HttpMethod.POST, body, String.class);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(1, customerRepository.count());
    }

    @Test
    void givenWrongCustomerId_whenGettingCustomer_thenThrowsError() {
        // given
        Long idParam = 1L;

        // when
        ResponseEntity<String> response = restTemplate.exchange("/api/customers/" + idParam,
                HttpMethod.GET, null, String.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, customerRepository.count());
    }

    @Test
    void givenValidCustomer_whenCreatingCustomer_thenCreatesCustomer() {
        // given
        CustomerDTO customerDto = new CustomerDTO("Customer name", "01234567890");
        HttpEntity<CustomerDTO> body = new HttpEntity<>(customerDto);

        // when
        ResponseEntity<CustomerModel> response =
                restTemplate.exchange("/api/customers", HttpMethod.POST, body, CustomerModel.class);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, customerRepository.count());
    }
}
