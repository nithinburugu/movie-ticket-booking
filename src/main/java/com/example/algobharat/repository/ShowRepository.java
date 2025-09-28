package com.example.algobharat.repository;

import com.example.algobharat.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByStartAtBetween(LocalDateTime from, LocalDateTime to);
}