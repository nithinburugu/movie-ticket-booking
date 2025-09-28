package com.example.algobharat.service;

import com.example.algobharat.dto.requests.BookingRequest;
import com.example.algobharat.dto.responses.BookingResponse;
import com.example.algobharat.model.*;
import com.example.algobharat.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final ShowSeatRepository showSeatRepo;
    private final ShowRepository showRepo;
    private final BookingRepository bookingRepo;
    private final TicketRepository ticketRepo;

    /**
     * Attempt to book `numSeats` contiguous seats for a show. Uses pessimistic
     * locking on candidate seats.
     */
    @Transactional
    public BookingResponse bookContiguous(BookingRequest req) {
        Long showId = req.getShowId();
        int numSeats = req.getNumSeats();
        String customerRef = req.getCustomerRef();

        // 1. fetch available seats for the show ordered by row and seat
        List<ShowSeat> available = showSeatRepo.findByShowIdAndStatusOrderBySeatRowAndNumber(showId,
                SeatStatus.AVAILABLE);

        if (available.isEmpty()) {
            return new BookingResponse(false, "No seats available for this show", null, null, null);
        }

        // group by row number to search contiguous runs
        Map<Integer, List<ShowSeat>> byRow = available.stream()
                .collect(Collectors.groupingBy(ss -> ss.getSeat().getRowNumber(), LinkedHashMap::new,
                        Collectors.toList()));

        for (Map.Entry<Integer, List<ShowSeat>> e : byRow.entrySet()) {
            List<ShowSeat> rowSeats = e.getValue();
            // ensure sorted by seat number
            rowSeats.sort(Comparator.comparingInt(ss -> ss.getSeat().getSeatNumber()));

            // sliding window
            for (int i = 0; i <= rowSeats.size() - numSeats; i++) {
                boolean contiguous = true;
                List<ShowSeat> window = new ArrayList<>();
                for (int j = 0; j < numSeats; j++) {
                    ShowSeat s = rowSeats.get(i + j);
                    window.add(s);
                    if (j > 0) {
                        int prev = rowSeats.get(i + j - 1).getSeat().getSeatNumber();
                        if (s.getSeat().getSeatNumber() != prev + 1) {
                            contiguous = false;
                            break;
                        }
                    }
                }
                if (!contiguous)
                    continue;

                List<Long> candidateIds = window.stream().map(ShowSeat::getId).collect(Collectors.toList());

                // lock candidate seats
                List<ShowSeat> locked = showSeatRepo.findAndLockByShowIdAndIds(showId, candidateIds);

                // verify still available
                boolean stillAvail = locked.stream().allMatch(s -> s.getStatus() == SeatStatus.AVAILABLE);
                if (!stillAvail) {
                    // someone else booked — try next window
                    continue;
                }

                // proceed with booking
                Show show = showRepo.findById(showId).orElseThrow(() -> new RuntimeException("Show not found"));
                Booking booking = new Booking();
                booking.setShow(show);
                booking.setCreatedAt(LocalDateTime.now());
                booking.setCustomerReference(customerRef);

                BigDecimal total = BigDecimal.ZERO;
                List<Ticket> tickets = new ArrayList<>();

                for (ShowSeat ss : locked) {
                    ss.setStatus(SeatStatus.BOOKED);
                    ss.setBooking(booking);
                    BigDecimal price = show.getPrice() != null ? show.getPrice() : show.getMovie().getBasePrice();
                    Ticket t = new Ticket();
                    t.setShowSeat(ss);
                    t.setBooking(booking);
                    t.setPrice(price);
                    tickets.add(t);
                    total = total.add(price);
                }

                booking.setTotalAmount(total);
                booking.setTickets(tickets);

                booking = bookingRepo.save(booking);
                ticketRepo.saveAll(tickets);
                showSeatRepo.saveAll(locked);

                List<String> labels = locked.stream().map(s -> s.getSeat().label()).collect(Collectors.toList());

                return new BookingResponse(true, "Booked successfully", booking.getId(), labels, total);
            }
        }

        return new BookingResponse(false, "No contiguous seats available for requested group size in this show", null,
                null, null);
    }

    // For suggestions, a helper that returns whether a show has contiguous block
    // and sample labels (no locks)
    public Optional<List<String>> findContiguousInShow(Long showId, int numSeats) {
        List<ShowSeat> available = showSeatRepo.findByShowIdAndStatusOrderBySeatRowAndNumber(showId,
                SeatStatus.AVAILABLE);
        Map<Integer, List<ShowSeat>> byRow = available.stream()
                .collect(Collectors.groupingBy(ss -> ss.getSeat().getRowNumber(), LinkedHashMap::new,
                        Collectors.toList()));
        for (List<ShowSeat> rowSeats : byRow.values()) {
            rowSeats.sort(Comparator.comparingInt(ss -> ss.getSeat().getSeatNumber()));
            for (int i = 0; i <= rowSeats.size() - numSeats; i++) {
                boolean contiguous = true;
                List<ShowSeat> window = new ArrayList<>();
                for (int j = 0; j < numSeats; j++) {
                    ShowSeat s = rowSeats.get(i + j);
                    window.add(s);
                    if (j > 0) {
                        int prev = rowSeats.get(i + j - 1).getSeat().getSeatNumber();
                        if (s.getSeat().getSeatNumber() != prev + 1) {
                            contiguous = false;
                            break;
                        }
                    }
                }
                if (!contiguous)
                    continue;
                return Optional.of(window.stream().map(s -> s.getSeat().label()).collect(Collectors.toList()));
            }
        }
        return Optional.empty();
    }
}
