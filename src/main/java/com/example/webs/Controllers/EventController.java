package com.example.webs.Controllers;

import com.example.webs.Services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Enable CORS for this controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Endpoint to get all events from the RDF file in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllEvents() {
        String result = eventService.getEvents();
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new event
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addEvent(@RequestBody Map<String, Object> newEvent) {
        String id = generateRandomString(12);
        String eventName = (String) newEvent.get("Name");
        String eventDescription = (String) newEvent.get("Description");
        String eventDate = (String) newEvent.get("Date");

        eventService.addEvent(id, eventName, eventDescription, eventDate);
        return ResponseEntity.ok("Event added successfully!");
    }

    // Endpoint to update an existing event
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEvent(@PathVariable String id, @RequestBody Map<String, Object> updatedEvent) {
        String eventName = (String) updatedEvent.get("Name");
        String eventDescription = (String) updatedEvent.get("Description");
        String eventDate = (String) updatedEvent.get("Date");

        eventService.updateEvent(id, eventName, eventDescription, eventDate);
        return ResponseEntity.ok("Event updated successfully!");
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
}
