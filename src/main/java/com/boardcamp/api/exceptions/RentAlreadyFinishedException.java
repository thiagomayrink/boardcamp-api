package com.boardcamp.api.exceptions;

public class RentAlreadyFinishedException extends RuntimeException {
    public RentAlreadyFinishedException(String message) {
        super(message);
    }
}
