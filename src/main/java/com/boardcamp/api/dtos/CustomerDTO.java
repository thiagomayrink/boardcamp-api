package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDTO {
    @NotBlank(message = "name is mandatory and must not be less then 2 letters")
    @Size(min = 2)
    private String name;

    @Size(min = 11, max = 11, message = "CPF must be only numbers")
    @NotBlank(message = "CPF is mandatory and must be valid")
    private String cpf;
}
