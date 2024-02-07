package com.boardcamp.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameDTO {
    @NotBlank(message = "name is mandatory and must be greater then 1 letters")
    private String name;

    @NotBlank(message = "image is mandatory and must be a valid URL")
    private String image;

    @Min(value = 1)
    @NotNull
    private int stockTotal;

    @Min(value = 1)
    @NotNull
    private int pricePerDay;
}
