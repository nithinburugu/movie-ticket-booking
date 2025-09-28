package com.example.algobharat.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "show_seats", uniqueConstraints = @UniqueConstraint(columnNames = { "show_id", "seat_id" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Show show;

    @ManyToOne
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @ManyToOne
    private Booking booking; // null unless booked
}