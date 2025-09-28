Algo Bharat Movie Ticket Booking API
Overview
This API powers the Algo Bharat movie ticket booking system, deployed and live at https://movie-ticket-booking-2-f1ez.onrender.com/. It provides endpoints for managing movies, theaters, halls, seats, shows, bookings, suggestions, analytics, and seat availability.
Endpoints

POST /api/v1/movies: Create a new movie.
Method: POST
URL: https://movie-ticket-booking-2-f1ez.onrender.com/api/v1/movies
Body: {
  "title": "Octopus",
  "durationMinutes": 148,
  "description": "A mind-bending thriller",
  "basePrice": 10.00
}


Response: 200 Created with the movie object (e.g., [{"id":1,"title":"Octopus","durationMinutes":148,"description":"A mind-bending thriller","basePrice":10.00}]).


POST /api/v1/theaters: Create a new theater.
Method: POST
URL: https://movie-ticket-booking-2-f1ez.onrender.com/api/v1/theaters
Headers: Content-Type: application/json
Body: {
  "name": "Asian Cinema",
  "address": "Pune"
}


Response: 200 Created with the theater object.


POST /api/v1/theaters/{theaterId}/halls: Create a new hall for a theater.
Method: POST
URL: https://movie-ticket-booking-2-f1ez.onrender.com/api/v1/theaters/{theaterId}/halls
Headers: Content-Type: application/json, Authorization: Basic YWRtaW46YWRtaW4=
Body: {
  "name": "Hall A",
  "theater": {
    "id": 1,
    "name": "PVR Cinemas",
    "address": "Pune"
  }
}


Response: 200 Created with the hall object.


POST /api/v1/theaters/halls/{hallId}/rows: Add rows to a hall.
Method: POST
URL: https://movie-ticket-booking-2-f1ez.onrender.com/api/v1/theaters/halls/{hallId}/rows
Headers: Content-Type: application/json
Body: {
  "rowNumber": 1,
  "seatCount": 6
}


Response: 200 Created with the row details.


POST /api/v1/theaters/halls/{hallId}/generateSeats: Generate seats for a hall.
Method: POST
URL: https://movie-ticket-booking-2-f1ez.onrender.com/api/v1/theaters/halls/{hallId}/generateSeats
Headers: Content-Type: application/json
Body: (No body required)
Response: 200 OK with seat generation status.


POST /api/v1/shows: Create a new show.
Method: POST
URL: https://movie-ticket-booking-2-f1ez.onrender.com/api/v1/shows
Headers: Content-Type: application/json
Body: {
  "movie": {
    "id": 1
  },
  "hall": {
    "id": 1
  },
  "startAt": "2025-10-01T18:00:00",
  "endAt": "2025-10-01T20:28:00",
  "price": 12.50
}


Response: 200 Created with the show object (e.g., [{"hall":"Hall A","seatLabels":["R1-S4","R1-S5","R1-S6"],"startAt":"2025-10-01T18:00:00","showId":1}]).


POST /api/v1/bookings: Book tickets for a show.
Method: POST
URL: https://movie-ticket-booking-2-f1ez.onrender.com/api/v1/bookings
Headers: Content-Type: application/json
Body: {
  "showId": 1,
  "numSeats": 3,
  "customerRef": "user1239"
}


Response: 200 OK with booking details.


GET /api/v1/shows/{showId}/suggestions: Get alternative show suggestions.
Method: GET
URL: https://movie-ticket-booking-2-f1ez.onrender.com/api/v1/shows/{showId}/suggestions?groupSize={groupSize}&timeWindowMinutes=120
Query Params: groupSize={groupSize}, timeWindowMinutes=120
Response: 200 OK with suggestion list.


GET /api/v1/analytics/movie/{movieId}: Get movie analytics.
Method: GET
URL: https://movie-ticket-booking-2-f1ez.onrender.com/api/v1/analytics/movie/{movieId}?from=2025-09-28&to=2025-09-29
Query Params: from=2025-09-28, to=2025-09-29
Response: 200 OK with analytics data (e.g., {"ticketsSold":0,"from":"2025-09-28","to":"2025-09-29","movieId":1,"gmv":0}).


GET /api/v1/theaters/halls/{hallid}/seats: Get available seats for a hall.
Method: GET
URL: https://movie-ticket-booking-2-f1ez.onrender.com/api/v1/theaters/halls/{hallid}/seats
Headers: Authorization: Basic YWRtaW46YWRtaW4=
Response: 200 OK with seat availability data (e.g., [] if no seats).



Deployment

URL: https://movie-ticket-booking-2-f1ez.onrender.com/
Platform: Render.com (free tier)
Note: The app may sleep after 15 minutes of inactivity. Use a ping service (e.g., UptimeRobot) to keep it awake.

Authentication

Basic Auth with username admin and password admin.

Postman Collection
Download the Postman collection for easy testing: testusingrok.postman_collection.json
