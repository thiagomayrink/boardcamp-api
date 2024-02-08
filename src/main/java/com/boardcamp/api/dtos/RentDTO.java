package com.boardcamp.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentDTO {
    @Min(value = 1)
    @NotNull
    private Long customerId;

    @Min(value = 1)
    @NotNull
    private Long gameId;

    @Min(value = 1)
    @NotNull
    private int daysRented;
}
