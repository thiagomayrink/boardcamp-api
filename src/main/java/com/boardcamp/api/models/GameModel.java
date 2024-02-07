package com.boardcamp.api.models;

import com.boardcamp.api.dtos.GameDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "games")
public class GameModel {

    public GameModel(GameDTO dto) {
        this.name = dto.getName();
        this.image = dto.getImage();
        this.pricePerDay = dto.getPricePerDay();
        this.stockTotal = dto.getStockTotal();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String image;

    @Positive
    @Column(nullable = false)
    private int stockTotal;

    @Positive
    @Column(nullable = false)
    private int pricePerDay;
}
