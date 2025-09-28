package com.example.algobharat.repository;

import com.example.algobharat.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByHallIdOrderByRowNumberAscSeatNumberAsc(Long hallId);

    List<Seat> findByHallIdAndRowNumberOrderBySeatNumberAsc(Long hallId, Integer rowNumber);
}