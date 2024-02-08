package com.boardcamp.api.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.boardcamp.api.models.RentModel;

@Repository
public interface RentRepository extends JpaRepository<RentModel, Long> {

    @Query("SELECT r FROM RentModel r WHERE r.game.id = :gameId AND r.returnDate IS NULL")
    List<RentModel> findActiveRentsByGameId(@Param("gameId") Long gameId);
}
