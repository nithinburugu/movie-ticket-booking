// package com.example.algobharat.controller;

// import com.example.algobharat.repository.TicketRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/v1/analytics")
// @RequiredArgsConstructor
// public class AnalyticsController {
//     private final TicketRepository ticketRepository;

//     @GetMapping("/movie/{movieId}")
//     public ResponseEntity<Map<String, Object>> movieStats(@PathVariable Long movieId,
//             @RequestParam String from,
//             @RequestParam String to) {
//         LocalDateTime f = LocalDate.parse(from).atStartOfDay();
//         LocalDateTime t = LocalDate.parse(to).atTime(23, 59, 59);
//         Object[] res = ticketRepository.countAndGmvForMovieBetween(movieId, f, t);
//         Long tickets = ((Number) res[0]).longValue();
//         java.math.BigDecimal gmv = (java.math.BigDecimal) res[1];
//         if (gmv == null)
//             gmv = java.math.BigDecimal.ZERO;
//         return ResponseEntity.ok(Map.of(
//                 "movieId", movieId,
//                 "from", from,
//                 "to", to,
//                 "ticketsSold", tickets,
//                 "gmv", gmv));
//     }
// }

package com.example.algobharat.controller;

import com.example.algobharat.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final TicketRepository ticketRepository;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Map<String, Object>> movieStats(@PathVariable Long movieId,
            @RequestParam String from,
            @RequestParam String to) {
        LocalDateTime f = LocalDate.parse(from).atStartOfDay();
        LocalDateTime t = LocalDate.parse(to).atTime(23, 59, 59);
        Object[] res = ticketRepository.countAndGmvForMovieBetween(movieId, f, t);

        // Log the query result for debugging
        System.out.println("Query result: " + (res == null ? "null" : java.util.Arrays.toString(res)));

        // Initialize defaults
        Long tickets = 0L;
        BigDecimal gmv = BigDecimal.ZERO;

        // Safely handle the result array
        if (res != null && res.length >= 1) {
            // Handle ticket count (res[0])
            if (res[0] != null) {
                if (res[0] instanceof Number) {
                    tickets = ((Number) res[0]).longValue();
                } else {
                    try {
                        tickets = Long.parseLong(res[0].toString());
                    } catch (NumberFormatException e) {
                        System.err.println("Failed to parse ticket count: " + res[0]);
                    }
                }
            }
            // Handle GMV (res[1]) if present
            if (res.length > 1 && res[1] != null) {
                if (res[1] instanceof BigDecimal) {
                    gmv = (BigDecimal) res[1];
                } else if (res[1] instanceof Number) {
                    gmv = new BigDecimal(((Number) res[1]).doubleValue());
                } else {
                    try {
                        gmv = new BigDecimal(res[1].toString());
                    } catch (NumberFormatException e) {
                        System.err.println("Failed to parse GMV: " + res[1]);
                    }
                }
            }
        } else {
            System.err.println("Query returned null or empty result for movieId: " + movieId);
        }

        return ResponseEntity.ok(Map.of(
                "movieId", movieId,
                "from", from,
                "to", to,
                "ticketsSold", tickets,
                "gmv", gmv));
    }
}
