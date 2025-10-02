package com.example.algobharat.controller;

import com.example.algobharat.model.Hall;
import com.example.algobharat.model.HallRow;
import com.example.algobharat.model.Seat;
import com.example.algobharat.repository.HallRepository;
import com.example.algobharat.repository.SeatRepository;
import com.example.algobharat.service.HallService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theaters")
@RequiredArgsConstructor
public class HallController {
    private final HallRepository hallRepository;
    private final HallService hallService;
    private final SeatRepository seatRepository;

    @PostMapping("/{theaterId}/halls")
    public ResponseEntity<Hall> addHall(@PathVariable Long theaterId, @RequestBody Hall hall) {
        // attach theater id (simplified: pass theater with id in payload or populate
        // externally)
        return ResponseEntity.ok(hallService.createHall(hall));
    }

    @PostMapping("/halls/{hallId}/rows")
    public ResponseEntity<HallRow> addRow(@PathVariable Long hallId, @RequestBody RowDto dto) {
        HallRow r = hallService.addRow(hallId, dto.getRowNumber(), dto.getSeatCount());
        return ResponseEntity.ok(r);
    }

    @PostMapping("/halls/{hallId}/generateSeats")
    public ResponseEntity<List<Seat>> generateSeats(@PathVariable Long hallId) {
        List<Seat> created = hallService.generateSeats(hallId);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/halls/{hallId}/seats")
    public ResponseEntity<List<Seat>> viewSeats(@PathVariable Long hallId) {
        hallRepository.findById(hallId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hall not found"));
        List<Seat> seats = seatRepository.findByHallIdOrderByRowNumberAscSeatNumberAsc(hallId);
        return ResponseEntity.ok(seats);
    }

    @Data
    static class RowDto {
        private Integer rowNumber;
        private Integer seatCount;
    }
}
