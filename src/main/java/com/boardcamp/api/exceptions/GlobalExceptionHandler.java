package com.boardcamp.api.exceptions;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // DTO Validations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception) {
        Map<String, String> map = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(
                fieldError -> map.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    // Customer
    @ExceptionHandler({CustomerNotFoundException.class})
    public ResponseEntity<String> handleCustomerNotFound(CustomerNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler({CustomerAlreadyExistsException.class})
    public ResponseEntity<String> handleCustomerAlreadyExists(
            CustomerAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    // Game
    @ExceptionHandler({GameAlreadyExistsException.class})
    public ResponseEntity<String> handleGameAlreadyExists(GameAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler({GameNotFoundException.class})
    public ResponseEntity<String> handleGameNotFound(GameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler({GameNotAvailableException.class})
    public ResponseEntity<String> handleGameNotAvailable(GameNotAvailableException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }

    // Rent
    @ExceptionHandler({RentNotFoundException.class})
    public ResponseEntity<String> handleRentNotFound(RentNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler({RentAlreadyFinishedException.class})
    public ResponseEntity<String> handleRentAlreadyFinished(
            RentAlreadyFinishedException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }
}
