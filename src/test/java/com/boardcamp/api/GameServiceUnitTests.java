package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.exceptions.GameAlreadyExistsException;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.services.GameService;

@SpringBootTest
@SuppressWarnings("null")
class GameServiceUnitTests {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository GameRepository;

    @Test
    void givenValidGame_whenCreatingGame_thenCreatesGame() {
        // given
        GameDTO GameDto = new GameDTO("GAME_NAME", "https://image-url.com", 1, 100);
        GameModel newGame = new GameModel(1L, "GAME_NAME", "https://image-url.com", 1, 100);

        doReturn(false).when(GameRepository).existsByName(any());
        doReturn(newGame).when(GameRepository).save(any());

        // when
        GameModel result = gameService.create(GameDto);

        // then
        assertNotNull(result);
        verify(GameRepository, times(1)).existsByName(any());
        verify(GameRepository, times(1)).save(any());
        assertEquals(newGame, result);
    }

    @Test
    void givenRepeatedGameName_whenCreatingGame_thenThrowsError() {
        // given
        GameDTO GameDto = new GameDTO("GAME_NAME", "https://image-url.com", 1, 100);

        doReturn(true).when(GameRepository).existsByName(any());

        // when
        GameAlreadyExistsException exception =
                assertThrows(GameAlreadyExistsException.class, () -> gameService.create(GameDto));

        // then
        assertNotNull(exception);
        assertEquals("Game already exists", exception.getMessage());
        verify(GameRepository, times(0)).save(any());
        verify(GameRepository, times(1)).existsByName(any());
    }

    @Test
    void givenNoGamesExists_whenGettingGame_thenReturnEmptyList() {
        // given
        List<GameModel> games = Collections.emptyList();
        doReturn(games).when(GameRepository).findAll();

        // when
        List<GameModel> result = gameService.listAll();

        // then
        assertNotNull(result);
        assertEquals(games, result);
        verify(GameRepository, times(1)).findAll();
    }
}
