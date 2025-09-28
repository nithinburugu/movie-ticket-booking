package com.example.algobharat.service;

import com.example.algobharat.model.*;
import com.example.algobharat.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HallService {
    private final HallRepository hallRepository;
    private final HallRowRepository hallRowRepository;
    private final SeatRepository seatRepository;

    public Hall createHall(Hall hall) {
        return hallRepository.save(hall);
    }

    public HallRow addRow(Long hallId, Integer rowNumber, Integer seatCount) {
        Hall hall = hallRepository.findById(hallId).orElseThrow(() -> new RuntimeException("Hall not found"));
        if (seatCount < 6)
            throw new IllegalArgumentException("Each row must have at least 6 seats");
        HallRow row = new HallRow();
        row.setHall(hall);
        row.setRowNumber(rowNumber);
        row.setSeatCount(seatCount);
        return hallRowRepository.save(row);
    }

    /**
     * Generate Seat entities from hall rows (initial setup). Idempotent-ish: will
     * not duplicate identical seat records.
     */
    @Transactional
    public List<Seat> generateSeats(Long hallId) {
        List<HallRow> rows = hallRowRepository.findByHallIdOrderByRowNumberAsc(hallId);
        Hall hall = hallRepository.findById(hallId).orElseThrow(() -> new RuntimeException("Hall not found"));
        List<Seat> created = new ArrayList<>();
        for (HallRow r : rows) {
            for (int sx = 1; sx <= r.getSeatCount(); sx++) {
                final int sn = sx;
                // check existence
                List<Seat> existing = seatRepository.findByHallIdAndRowNumberOrderBySeatNumberAsc(hallId,
                        r.getRowNumber());
                boolean exists = existing.stream().anyMatch(s -> s.getSeatNumber().equals(sn));
                if (!exists) {
                    Seat s = new Seat();
                    s.setHall(hall);
                    s.setRowNumber(r.getRowNumber());
                    s.setSeatNumber(sn);
                    created.add(s);
                }
            }
        }
        return seatRepository.saveAll(created);
    }
}
