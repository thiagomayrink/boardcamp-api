package com.boardcamp.api.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.exceptions.GameAlreadyExistsException;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.repositories.GameRepository;

@Service
public class GameService {
    final GameRepository gameRepository;

    GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameModel create(GameDTO dto) {
        if (gameRepository.existsByName(dto.getName())) {
            throw new GameAlreadyExistsException("Game already exists");
        }
        GameModel game = new GameModel(dto);
        return gameRepository.save(game);
    }

    public List<GameModel> listAll() {
        return gameRepository.findAll();
    }
}
