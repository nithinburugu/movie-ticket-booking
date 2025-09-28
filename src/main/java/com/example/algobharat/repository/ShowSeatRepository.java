package com.example.algobharat.repository;

import com.example.algobharat.model.SeatStatus;
import com.example.algobharat.model.ShowSeat;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    @Query("select s from ShowSeat s join fetch s.seat seat where s.show.id = :showId and s.status = :status order by seat.rowNumber asc, seat.seatNumber asc")
    List<ShowSeat> findByShowIdAndStatusOrderBySeatRowAndNumber(@Param("showId") Long showId,
            @Param("status") SeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ShowSeat s where s.show.id = :showId and s.id in :ids")
    List<ShowSeat> findAndLockByShowIdAndIds(@Param("showId") Long showId, @Param("ids") List<Long> ids);
}