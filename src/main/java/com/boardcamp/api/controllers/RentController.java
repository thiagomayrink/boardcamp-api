package com.boardcamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.RentDTO;
import com.boardcamp.api.models.RentModel;
import com.boardcamp.api.services.RentService;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/rentals")
public class RentController {

    final RentService rentService;

    RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping
    public ResponseEntity<RentModel> createRent(@RequestBody @Valid RentDTO body) {
        return new ResponseEntity<>(rentService.create(body), new HttpHeaders(),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RentModel>> listRents() {
        return new ResponseEntity<>(rentService.listAll(), new HttpHeaders(), HttpStatus.OK);
    }

}
