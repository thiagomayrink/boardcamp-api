package com.boardcamp.api.exceptions;

public class GameNotAvailableException extends RuntimeException {
    public GameNotAvailableException(String message) {
        super(message);
    }
}
