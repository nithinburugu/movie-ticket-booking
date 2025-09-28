package com.example.algobharat.service;

import com.example.algobharat.model.*;
import com.example.algobharat.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final ShowSeatRepository showSeatRepository;

    /**
     * Create show and initialize show seats by copying hall seats into show_seats
     * table
     */
    @Transactional
    public Show createShow(Show show) {
        Show saved = showRepository.save(show);
        // initialize show seats
        List<Seat> seats = seatRepository.findByHallIdOrderByRowNumberAscSeatNumberAsc(show.getHall().getId());
        System.out.println("\n\n\n Seats : " + seats.toString() + "\n\n\n");
        List<ShowSeat> ss = seats.stream().map(s -> {
            ShowSeat sh = new ShowSeat();
            sh.setShow(saved);
            sh.setSeat(s);
            sh.setStatus(SeatStatus.AVAILABLE);
            return sh;
        }).collect(Collectors.toList());
        showSeatRepository.saveAll(ss);
        System.out.println("\n\n\n SeatsShow  : " + ss.toString() + "\n\n\n");
        return saved;
    }
}

// @Service
// public class ShowService {
// private final ShowRepository showRepository;
// private final MovieRepository movieRepository;
// private final HallRepository hallRepository;

// public ShowService(ShowRepository showRepository, MovieRepository
// movieRepository, HallRepository hallRepository) {
// this.showRepository = showRepository;
// this.movieRepository = movieRepository;
// this.hallRepository = hallRepository;
// }

// public Show createShow(Show showRequest) {
// // Fetch the hall
// Hall hall = hallRepository.findById(showRequest.getHall().getId())
// .orElseThrow(() -> new RuntimeException("Hall not found"));

// // Fetch the movie
// Movie movie = movieRepository.findById(showRequest.getMovie().getId())
// .orElseThrow(() -> new RuntimeException("Movie not found"));

// // Set the actual entities
// showRequest.setHall(hall);
// showRequest.setMovie(movie);

// return showRepository.save(showRequest);
// }
// }
