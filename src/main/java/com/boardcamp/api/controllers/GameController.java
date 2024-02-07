package com.boardcamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.services.GameService;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/games")
public class GameController {

    final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameModel> createGame(@RequestBody @Valid GameDTO body) {
        return new ResponseEntity<>(gameService.create(body), new HttpHeaders(),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GameModel>> listGames() {
        return new ResponseEntity<>(gameService.listAll(), new HttpHeaders(), HttpStatus.OK);
    }

}
