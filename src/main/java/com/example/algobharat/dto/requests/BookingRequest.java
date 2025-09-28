package com.example.algobharat.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull
    private Long showId;
    @Min(1)
    private int numSeats;
    private String customerRef;
}