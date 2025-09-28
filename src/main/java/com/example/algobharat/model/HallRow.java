package com.example.algobharat.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hall_rows", uniqueConstraints = @UniqueConstraint(columnNames = { "hall_id", "row_number" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HallRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Hall hall;

    @Column(name = "row_number")
    private Integer rowNumber;

    private Integer seatCount; // >= 6
}