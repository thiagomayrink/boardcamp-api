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
import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.repositories.GameRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GameServiceIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    @AfterEach
    void cleanUpDatabase() {
        gameRepository.deleteAll();
    }

    @Test
    void givenValidGame_whenCreatingGame_thenCreatesGame() {
        // given
        GameDTO gameDto = new GameDTO("GAME_NAME", "https://image-url.com", 1, 100);
        HttpEntity<GameDTO> body = new HttpEntity<>(gameDto);

        // when
        ResponseEntity<GameModel> response =
                restTemplate.exchange("/api/games", HttpMethod.POST, body, GameModel.class);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, gameRepository.count());
    }

    @Test
    void givenRepeatedGameName_whenCreatingGame_thenThrowsError() {
        // given
        GameModel gameConflict = new GameModel(1L, "GAME_NAME", "https://image-url.com", 1, 100);
        gameRepository.save(gameConflict);

        GameDTO gameDto = new GameDTO("GAME_NAME", "https://image-url.com", 1, 100);
        HttpEntity<GameDTO> body = new HttpEntity<>(gameDto);

        // when
        ResponseEntity<String> response =
                restTemplate.exchange("/api/games", HttpMethod.POST, body, String.class);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(1, gameRepository.count());
    }

    @Test
    void givenInvalidGameName_whenCreatingGame_thenThrowsError() {
        // given
        GameDTO gameDto = new GameDTO("", "https://image-url.com", 1, 100);
        HttpEntity<GameDTO> body = new HttpEntity<>(gameDto);

        // when
        ResponseEntity<String> response =
                restTemplate.exchange("/api/games", HttpMethod.POST, body, String.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(0, gameRepository.count());
    }

    @Test
    void givenInvalidStockTotal_whenCreatingGame_thenThrowsError() {
        // given
        GameDTO gameDto = new GameDTO("GAME_NAME", "https://image-url.com", -1, 100);
        HttpEntity<GameDTO> body = new HttpEntity<>(gameDto);

        // when
        ResponseEntity<String> response =
                restTemplate.exchange("/api/games", HttpMethod.POST, body, String.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(0, gameRepository.count());
    }

    @Test
    void givenInvalidPricePerDay_whenCreatingGame_thenThrowsError() {
        // given
        GameDTO gameDto = new GameDTO("GAME_NAME", "https://image-url.com", 1, -100);
        HttpEntity<GameDTO> body = new HttpEntity<>(gameDto);

        // when
        ResponseEntity<String> response =
                restTemplate.exchange("/api/games", HttpMethod.POST, body, String.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(0, gameRepository.count());
    }
}
