package com.boardcamp.api.exceptions;

public class RentNotFoundException extends RuntimeException {
    public RentNotFoundException(String message) {
        super(message);
    }
}
