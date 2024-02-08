package com.boardcamp.api.models;

import java.time.LocalDate;
import com.boardcamp.api.dtos.RentDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rentals")
public class RentModel {
    public RentModel(RentDTO dto, CustomerModel customer, GameModel game, LocalDate rentDate,
            LocalDate returnDate, int originalPrice, int delayFee) {
        this.rentDate = rentDate;
        this.daysRented = dto.getDaysRented();
        this.returnDate = returnDate;
        this.originalPrice = originalPrice;
        this.delayFee = delayFee;
        this.customer = customer;
        this.game = game;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private LocalDate rentDate;

    @Positive()
    @Column(nullable = false)
    private int daysRented;

    @Column()
    private LocalDate returnDate;

    @Positive()
    @Column(nullable = false)
    private int originalPrice;

    @Column(nullable = false)
    private int delayFee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customerId")
    private CustomerModel customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gameId")
    private GameModel game;
}
