package com.example.webs.Controllers;

import com.example.webs.Enums.ParticipationType;
import com.example.webs.Services.ParticipationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/participations")
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    // Class-level logger
    private static final Logger logger = LoggerFactory.getLogger(ParticipationController.class);

    // Endpoint to add a new participation
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addParticipation(@RequestBody String participationData) {
        try {
            // Parse the request body into JSON
            JSONObject jsonObject = new JSONObject(participationData);

            // Retrieve fields from the JSON object
            String participantName = jsonObject.getString("participantName");
            String eventId = jsonObject.getString("eventId");

            // Convert participation type to enum
            String participationTypeStr = jsonObject.getString("participationType").replace(" ", "_").toUpperCase();
            ParticipationType participationType;
            try {
                participationType = ParticipationType.valueOf(participationTypeStr);
            } catch (IllegalArgumentException ex) {
                logger.error("Invalid participation type provided: {}", participationTypeStr);
                // Return error in JSON format
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Invalid participation type provided.");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Check if the event exists
            if (!participationService.eventExists(eventId)) {
                // Return error in JSON format
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Event does not exist with ID: " + eventId);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            logger.info("Adding Participation - ParticipantName: {}, EventID: {}, ParticipationType: {}",
                    participantName, eventId, participationType);

            // Call the service to add participation (ID is generated automatically in the service)
            participationService.addParticipation(participantName, eventId, participationType);

            // Return success response in JSON format
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Participation added successfully.");
            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            logger.error("Error adding participation: {}", e.getMessage());
            // Return error in JSON format
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error adding participation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Endpoint to retrieve all participations
    @GetMapping
    public ResponseEntity<String> getAllParticipations() {
        String result = participationService.getParticipations();
        return ResponseEntity.ok(result);
    }

    // Endpoint to delete a participation by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteParticipation(@PathVariable String id) {
        participationService.deleteParticipation(id);
        return ResponseEntity.ok("Participation deleted successfully!");
    }

    // Endpoint to retrieve participations by event ID
    @GetMapping("/event/{eventId}")
    public ResponseEntity<String> getParticipationByEvent(@PathVariable String eventId) {
        String result = participationService.getParticipationByEvent(eventId);
        return ResponseEntity.ok(result);
    }

    // New endpoint to retrieve participations grouped by all events
    @GetMapping("/byEvents")
    public ResponseEntity<String> getParticipationsByAllEvents() {
        try {
            String result = participationService.getParticipationsByAllEvents();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error retrieving participations by events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving participations by events.");
        }
    }
}
