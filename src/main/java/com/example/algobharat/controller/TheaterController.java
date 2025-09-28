package com.example.algobharat.controller;

import com.example.algobharat.model.Theater;
import com.example.algobharat.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/theaters")
@RequiredArgsConstructor
public class TheaterController {
    private final TheaterRepository theaterRepository;

    @PostMapping
    public ResponseEntity<Theater> create(@RequestBody Theater theater) {
        return ResponseEntity.ok(theaterRepository.save(theater));
    }
}
