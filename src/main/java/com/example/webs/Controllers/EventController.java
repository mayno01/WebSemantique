package com.example.webs.Controllers;

import com.example.webs.Enums.EventType;
import com.example.webs.Services.EventService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Random;
import org.springframework.http.MediaType;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Endpoint to add a new event


    // Endpoint to add a new event
    @PostMapping("/add")
    public ResponseEntity<String> addEvent(@RequestBody String eventData) {
        Logger logger = LoggerFactory.getLogger(EventController.class);

        try {
            // Parse the incoming JSON data
            JSONObject eventJson = new JSONObject(eventData);

            // Generate a random ID (you can also use another strategy like UUID)
            String generatedId = generateEventId();

            // Get event data from the JSON
            String name = eventJson.getString("name");
            String description = eventJson.getString("description");
            String date = eventJson.getString("date");
            EventType eventType = EventType.valueOf(eventJson.getString("eventType"));

            // Add event to the service with the generated ID
            eventService.addEvent(generatedId, name, description, date, eventType);

            // Return success response with the generated ID
            JSONObject responseJson = new JSONObject();
            responseJson.put("id", generatedId);
            responseJson.put("message", "Event added successfully");
            return ResponseEntity.ok(responseJson.toString());
        } catch (Exception e) {
            // Handle error (e.g., invalid data in the request)
            logger.error("Error adding event: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("{\"message\": \"Error adding event: " + e.getMessage() + "\"}");
        }
    }

    // Helper method to generate a random event ID (You can use other strategies here)
    private String generateEventId() {
        Random random = new Random();
        int randomId = random.nextInt(100000); // Generate a random 5-digit number
        return String.format("event-%d", randomId);
    }
    // Endpoint to update an existing event
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable String id, @RequestBody String eventData) {
        Logger logger = LoggerFactory.getLogger(EventController.class);

        try {
            JSONObject jsonObject = new JSONObject(eventData);
            String name = jsonObject.getString("name");
            String description = jsonObject.getString("description");
            String date = jsonObject.getString("date");

            // Parse EventType from JSON input
            EventType eventType;
            try {
                eventType = EventType.valueOf(jsonObject.getString("eventType").toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.error("Invalid event type provided: {}", jsonObject.getString("eventType"));
                return ResponseEntity.badRequest().body("Invalid event type provided.");
            }

            logger.info("Updating Event - ID: {}, Name: {}, Description: {}, Date: {}, EventType: {}",
                    id, name, description, date, eventType);

            // Pass the id to the service to update the event
            eventService.updateEvent(id, name, description, date, eventType);

            return ResponseEntity.ok("Event updated successfully.");
        } catch (Exception e) {
            logger.error("Error updating event: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error updating event: " + e.getMessage());
        }
    }

    // Endpoint to get all events from the RDF file in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllEvents() {
        String result = eventService.getEvents();
        return ResponseEntity.ok(result);
    }
    // Endpoint to delete an event by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully!");
    }

    // Utility method to generate a random string ID
    private static final String CHARACTERS = "0123456789";
    private static final Random RANDOM = new Random();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
    // Endpoint to get an event by its ID
    @GetMapping("/{id}")
    public ResponseEntity<String> getEventById(@PathVariable String id) {
        Logger logger = LoggerFactory.getLogger(EventController.class);

        try {
            // Call the service to get the event by ID
            String result = eventService.getEventById(id);

            if (result == null || result.isEmpty()) {
                return ResponseEntity.status(404).body("Event not found with ID: " + id);
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Error retrieving event by ID: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving event by ID: " + e.getMessage());
        }
    }
    @GetMapping("/type/{eventType}")
    public ResponseEntity<String> getEventByEventType(@PathVariable String eventType) {
        Logger logger = LoggerFactory.getLogger(EventController.class);

        try {
            // Normalize eventType to avoid case and spacing issues
            String eventTypeStr = eventType.replace(" ", "_").toUpperCase();
            EventType eventTypeEnum;

            try {
                eventTypeEnum = EventType.valueOf(eventTypeStr);
            } catch (IllegalArgumentException ex) {
                logger.error("Invalid event type provided: {}", eventTypeStr);
                return ResponseEntity.badRequest().body("Invalid event type provided.");
            }

            // Call the service to get events by eventType
            String result = eventService.getEventByEventType(eventTypeEnum);

            if (result == null || result.isEmpty()) {
                return ResponseEntity.status(404).body("No events found for type: " + eventTypeEnum);
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Error retrieving events by eventType: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving events by eventType: " + e.getMessage());
        }
    }

}
