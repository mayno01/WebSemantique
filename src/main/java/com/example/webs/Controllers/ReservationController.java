package com.example.webs.Controllers;

import com.example.webs.Services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Adjust CORS settings as needed
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Retrieve all reservations
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllReservations() {
        String result = reservationService.getReservations();
        return ResponseEntity.ok(result);
    }

    // Add a new reservation
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addReservation(@RequestBody Map<String, Object> newReservationData) {
        String reservationId = (String) newReservationData.get("Reservation_id");
        String reservationType = (String) newReservationData.get("Reservation_type");
        String reservationDate = (String) newReservationData.get("Reservation_date");
        String reservationDescription = (String) newReservationData.get("Reservation_description");
        String reservationFeedback = (String) newReservationData.get("Reservation_feedback");

        try {
            reservationService.addReservation(reservationId, reservationType, reservationDate, reservationDescription, reservationFeedback);
            return ResponseEntity.ok("Reservation added successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Invalid Reservation type provided");
        }
    }

    // Update an existing reservation
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateReservation(@PathVariable String id, @RequestBody Map<String, Object> updatedReservationData) {
        String reservationType = (String) updatedReservationData.get("Reservation_type");
        String reservationDate = (String) updatedReservationData.get("Reservation_date");
        String reservationDescription = (String) updatedReservationData.get("Reservation_description");
        String reservationFeedback = (String) updatedReservationData.get("Reservation_feedback");

        try {
            reservationService.updateReservation(id, reservationType, reservationDate, reservationDescription, reservationFeedback);
            return ResponseEntity.ok("Reservation updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Invalid Reservation type provided");
        }
    }

    // Delete a reservation
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable String id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok("Reservation deleted successfully!");
    }
}
