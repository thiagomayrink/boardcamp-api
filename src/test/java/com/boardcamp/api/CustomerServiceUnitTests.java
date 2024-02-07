package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.exceptions.CustomerAlreadyExistsException;
import com.boardcamp.api.exceptions.CustomerNotFoundException;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.services.CustomerService;

@SpringBootTest
@SuppressWarnings("null")
class CustomerServiceUnitTests {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void givenRepeatedCustomerCpf_whenCreatingCustomer_thenThrowsError() {
        // given
        CustomerDTO customerDto = new CustomerDTO("Customer name", "01234567890");

        doReturn(true).when(customerRepository).existsByCpf(any());

        // when
        CustomerAlreadyExistsException exception = assertThrows(
                CustomerAlreadyExistsException.class, () -> customerService.create(customerDto));

        // then
        assertNotNull(exception);
        assertEquals("Customer already exists", exception.getMessage());
        verify(customerRepository, times(0)).save(any());
        verify(customerRepository, times(1)).existsByCpf(any());
    }

    @Test
    void givenWrongCustomerId_whenGettingCustomer_thenThrowsError() {
        // given
        Long idParam = 1L;

        // when
        doReturn(Optional.empty()).when(customerRepository).findById(idParam);
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
                () -> customerService.findById(idParam));

        // then
        assertNotNull(exception);
        assertEquals("Customer not found with id: " + idParam, exception.getMessage());
        verify(customerRepository, times(1)).findById(idParam);
    }

    @Test
    void givenValidCustomer_whenCreatingCustomer_thenCreatesCustomer() {
        // given
        CustomerDTO customerDto = new CustomerDTO("Customer name", "01234567890");
        CustomerModel newCustomer = new CustomerModel(1L, "Customer name", "01234567890");

        doReturn(false).when(customerRepository).existsByCpf(any());
        doReturn(newCustomer).when(customerRepository).save(any());

        // when
        CustomerModel result = customerService.create(customerDto);

        // then
        assertNotNull(result);
        verify(customerRepository, times(1)).existsByCpf(any());
        verify(customerRepository, times(1)).save(any());
        assertEquals(newCustomer, result);
    }
}
