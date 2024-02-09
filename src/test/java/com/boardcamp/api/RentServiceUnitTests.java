package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.RentDTO;
import com.boardcamp.api.exceptions.GameNotAvailableException;
import com.boardcamp.api.exceptions.RentAlreadyFinishedException;
import com.boardcamp.api.exceptions.RentNotFoundException;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.models.RentModel;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.repositories.RentRepository;
import com.boardcamp.api.services.RentService;

@SpringBootTest
@SuppressWarnings("null")
class RentServiceUnitTests {

    @InjectMocks
    private RentService rentService;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void givenValidRent_whenCreatingRent_thenCreatesRent() {
        // given
        RentDTO rentDto = new RentDTO(1L, 1L, 5);
        CustomerModel customer = new CustomerModel();
        GameModel game = new GameModel();
        game.setStockTotal(10);
        game.setPricePerDay(100);

        RentModel newRent = new RentModel(rentDto, customer, game, LocalDate.now(), null, 500, 0);

        doReturn(Optional.of(customer)).when(customerRepository).findById(any());
        doReturn(Optional.of(game)).when(gameRepository).findById(any());
        doReturn(Collections.emptyList()).when(rentRepository).findActiveRentsByGameId(any());
        doReturn(newRent).when(rentRepository).save(any());

        // when
        RentModel result = rentService.create(rentDto);

        // then
        assertNotNull(result);
        verify(customerRepository, times(1)).findById(any());
        verify(gameRepository, times(1)).findById(any());
        verify(rentRepository, times(1)).findActiveRentsByGameId(any());
        verify(rentRepository, times(1)).save(any());
        assertEquals(newRent, result);
    }

    @Test
    void givenValidRent_whenCreatingRent_thenCreatesRentWithCorrectValues() {
        // given
        RentDTO rentDto = new RentDTO(1L, 1L, 5);
        CustomerModel customer = new CustomerModel();
        GameModel game = new GameModel();
        game.setStockTotal(10);
        game.setPricePerDay(100);

        RentModel newRent = new RentModel(rentDto, customer, game, LocalDate.now(), null, 500, 0);

        doReturn(Optional.of(customer)).when(customerRepository).findById(any());
        doReturn(Optional.of(game)).when(gameRepository).findById(any());
        doReturn(Collections.emptyList()).when(rentRepository).findActiveRentsByGameId(any());
        doReturn(newRent).when(rentRepository).save(any());

        // when
        RentModel result = rentService.create(rentDto);

        // then
        assertNotNull(result);
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                result.getRentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertEquals(game.getPricePerDay() * rentDto.getDaysRented(), result.getOriginalPrice());
        assertNull(result.getReturnDate());
        assertEquals(0, result.getDelayFee());
    }

    @Test
    void givenGameNotAvailable_whenCreatingRent_thenThrowsError() {
        // given
        RentDTO rentDto = new RentDTO(1L, 1L, 5);
        CustomerModel customer = new CustomerModel();
        GameModel game = new GameModel();
        game.setStockTotal(0);
        game.setPricePerDay(100);

        doReturn(Optional.of(customer)).when(customerRepository).findById(any());
        doReturn(Optional.of(game)).when(gameRepository).findById(any());
        doReturn(Collections.emptyList()).when(rentRepository).findActiveRentsByGameId(any());

        // when
        GameNotAvailableException exception =
                assertThrows(GameNotAvailableException.class, () -> rentService.create(rentDto));

        // then
        assertNotNull(exception);
        assertEquals("The game is out of stock", exception.getMessage());
        verify(rentRepository, times(0)).save(any());
    }

    @Test
    void givenNoRentsExists_whenGettingRent_thenReturnEmptyList() {
        // given
        List<RentModel> rents = Collections.emptyList();
        doReturn(rents).when(rentRepository).findAll();

        // when
        List<RentModel> result = rentService.listAll();

        // then
        assertNotNull(result);
        assertEquals(rents, result);
        verify(rentRepository, times(1)).findAll();
    }

    @Test
    void givenInvalidRentId_whenFinishingRent_thenThrowsRentNotFoundException() {
        // given
        Long invalidRentId = 1L;

        doReturn(Optional.empty()).when(rentRepository).findById(any());

        // when
        RentNotFoundException exception =
                assertThrows(RentNotFoundException.class, () -> rentService.finish(invalidRentId));

        // then
        assertNotNull(exception);
        assertEquals("Rent not found with id: " + invalidRentId, exception.getMessage());
        verify(rentRepository, times(0)).save(any());
    }

    @Test
    void givenFinishedRent_whenFinishingRent_thenThrowsRentAlreadyFinishedException() {
        // given
        Long rentId = 1L;
        RentModel finishedRent = new RentModel();
        finishedRent.setReturnDate(LocalDate.now());

        doReturn(Optional.of(finishedRent)).when(rentRepository).findById(any());

        // when
        RentAlreadyFinishedException exception =
                assertThrows(RentAlreadyFinishedException.class, () -> rentService.finish(rentId));

        // then
        assertNotNull(exception);
        assertEquals("Rent already finished at date: " + finishedRent.getReturnDate(),
                exception.getMessage());
        verify(rentRepository, times(0)).save(any());
    }

    @Test
    void givenValidRent_whenFinishingRent_thenFinishesRent() {
        // given
        Long rentId = 1L;
        GameModel game = new GameModel(1L, "GAME_NAME", "https://image-url.com", 1, 100);
        CustomerModel customer = new CustomerModel(1L, "CUSTOMER_NAME", "01234567890");
        RentModel activeRent = new RentModel(rentId, LocalDate.now().minusDays(8), 5, null,
                5 * game.getPricePerDay(), 0, customer, game);

        doReturn(Optional.of(activeRent)).when(rentRepository).findById(rentId);

        ArgumentCaptor<RentModel> captor = ArgumentCaptor.forClass(RentModel.class);
        doAnswer(invocation -> {
            RentModel arg = captor.getValue();
            arg.setId(rentId);
            return arg;
        }).when(rentRepository).save(captor.capture());

        // when
        RentModel result = rentService.finish(rentId);

        // then
        assertNotNull(result);
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                result.getReturnDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertEquals(3 * 100, result.getDelayFee()); // delay fee is 3 days * 100 price per day
        verify(rentRepository, times(1)).save(any());
    }
}
