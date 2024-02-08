package com.boardcamp.api.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.RentDTO;
import com.boardcamp.api.exceptions.CustomerNotFoundException;
import com.boardcamp.api.exceptions.GameNotAvailableException;
import com.boardcamp.api.exceptions.GameNotFoundException;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.models.RentModel;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.repositories.RentRepository;

@Service
public class RentService {
    final RentRepository rentRepository;
    final GameRepository gameRepository;
    final CustomerRepository customerRepository;

    RentService(RentRepository rentRepository, GameRepository gameRepository,
            CustomerRepository customerRepository) {
        this.rentRepository = rentRepository;
        this.gameRepository = gameRepository;
        this.customerRepository = customerRepository;
    }

    private boolean isGameAvailable(GameModel game) {
        List<RentModel> rentals = rentRepository.findActiveRentsByGameId(game.getId());
        return game.getStockTotal() > rentals.size();
    }

    public RentModel create(RentDTO dto) {
        @SuppressWarnings("null")
        CustomerModel customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with id: " + dto.getGameId()));

        @SuppressWarnings("null")
        GameModel game = gameRepository.findById(dto.getGameId()).orElseThrow(
                () -> new GameNotFoundException("Game not found with id: " + dto.getGameId()));

        if (!this.isGameAvailable(game))
            throw new GameNotAvailableException("The game is out of stock");

        LocalDate renDate = LocalDate.now();
        int originalPrice = dto.getDaysRented() * game.getPricePerDay();
        LocalDate returnDate = null;
        int delayFee = 0;

        RentModel rent =
                new RentModel(dto, customer, game, renDate, returnDate, originalPrice, delayFee);
        return rentRepository.save(rent);
    }

    public List<RentModel> listAll() {
        return rentRepository.findAll();
    }
}
