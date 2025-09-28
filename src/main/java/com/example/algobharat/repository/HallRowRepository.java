package com.example.algobharat.repository;

import com.example.algobharat.model.HallRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HallRowRepository extends JpaRepository<HallRow, Long> {
    List<HallRow> findByHallIdOrderByRowNumberAsc(Long hallId);
}