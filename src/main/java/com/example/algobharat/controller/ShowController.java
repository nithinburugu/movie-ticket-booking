package com.example.algobharat.controller;

import com.example.algobharat.model.Show;
import com.example.algobharat.repository.ShowRepository;
import com.example.algobharat.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
public class ShowController {
    private final ShowRepository showRepository;
    private final ShowService showService;

    @PostMapping
    public ResponseEntity<Show> create(@RequestBody Show show) {
        Show saved = showService.createShow(show);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Show> get(@PathVariable Long id) {
        return showRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
