package com.boardcamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.services.CustomerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/customers")
public class CustomerController {

    final CustomerService customerService;

    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerModel> createCustomer(@RequestBody @Valid CustomerDTO body) {
        return new ResponseEntity<>(customerService.create(body), new HttpHeaders(),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerModel> getCustomerById(
            @PathVariable("id") @NotNull Long customerId) {
        return new ResponseEntity<>(customerService.findById(customerId), new HttpHeaders(),
                HttpStatus.OK);
    }

}
