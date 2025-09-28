package com.example.algobharat.repository;

import com.example.algobharat.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("select count(t), coalesce(sum(t.price),0) from Ticket t where t.showSeat.show.movie.id = :movieId and t.booking.createdAt between :from and :to")
    Object[] countAndGmvForMovieBetween(@Param("movieId") Long movieId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}