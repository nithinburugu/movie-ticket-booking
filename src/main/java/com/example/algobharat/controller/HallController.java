package com.example.algobharat.controller;

import com.example.algobharat.model.Hall;
import com.example.algobharat.model.HallRow;
import com.example.algobharat.model.Seat;
// import com.example.algobharat.repository.HallRepository;
import com.example.algobharat.service.HallService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theaters")
@RequiredArgsConstructor
public class HallController {
    // private final HallRepository hallRepository;
    private final HallService hallService;

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
        List<Seat> seats = hallService.generateSeats(hallId); // lightweight: to ensure seats exist, otherwise use seat
                                                              // repo directly
        return ResponseEntity.ok(seats);
    }

    @Data
    static class RowDto {
        private Integer rowNumber;
        private Integer seatCount;
    }
}
