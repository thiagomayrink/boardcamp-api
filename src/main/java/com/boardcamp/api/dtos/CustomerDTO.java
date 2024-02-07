package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDTO {
    @NotBlank(message = "name is mandatory and must be greater then 2 letters")
    @Size(min = 2, message = "name must be greater then 2 letters")
    private String name;

    @Size(min = 11, max = 11, message = "CPF must be only numbers and max 11 digits")
    @NotBlank(message = "CPF is mandatory and must be valid")
    private String cpf;
}
