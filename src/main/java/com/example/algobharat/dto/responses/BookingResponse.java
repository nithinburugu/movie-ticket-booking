package com.example.algobharat.dto.responses;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private boolean success;
    private String message;
    private Long bookingId;
    private List<String> seatLabels;
    private BigDecimal totalAmount;
}