package com.example.algobharat.controller;

import com.example.algobharat.dto.requests.BookingRequest;
import com.example.algobharat.dto.responses.BookingResponse;
import com.example.algobharat.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> book(@Valid @RequestBody BookingRequest req) {
        BookingResponse resp = bookingService.bookContiguous(req);
        if (resp.isSuccess())
            return ResponseEntity.ok(resp);
        else
            return ResponseEntity.status(409).body(resp);
    }
}
