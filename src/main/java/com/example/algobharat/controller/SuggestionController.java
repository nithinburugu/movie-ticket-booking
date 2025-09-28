package com.example.algobharat.controller;

import com.example.algobharat.service.BookingService;
import com.example.algobharat.repository.ShowRepository;
import com.example.algobharat.model.Show;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
public class SuggestionController {
    private final BookingService bookingService;
    private final ShowRepository showRepository;

    @GetMapping("/{showId}/suggestions")
    public ResponseEntity<List<Map<String, Object>>> suggestions(@PathVariable Long showId,
            @RequestParam int groupSize,
            @RequestParam(defaultValue = "120") int timeWindowMinutes) {
        // find original show
        Show orig = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show not found"));
        LocalDateTime from = orig.getStartAt().minusMinutes(timeWindowMinutes);
        LocalDateTime to = orig.getStartAt().plusMinutes(timeWindowMinutes);

        List<Show> candidates = showRepository.findByStartAtBetween(from, to);
        List<Map<String, Object>> suggestions = new ArrayList<>();
        for (Show s : candidates) {
            bookingService.findContiguousInShow(s.getId(), groupSize).ifPresent(labels -> {
                suggestions.add(Map.of(
                        "showId", s.getId(),
                        "startAt", s.getStartAt(),
                        "hall", s.getHall().getName(),
                        "seatLabels", labels));
            });
        }
        return ResponseEntity.ok(suggestions);
    }
}
