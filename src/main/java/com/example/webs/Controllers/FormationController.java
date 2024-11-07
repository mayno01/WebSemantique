package com.example.webs.Controllers;

import com.example.webs.Services.FormationService;
import com.example.webs.Enums.FormationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Enable CORS for this controller
@RequestMapping("/formations")
public class FormationController {

    @Autowired
    private FormationService formationService;

    // Endpoint to retrieve all formations in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllFormations() {
        String result = formationService.getFormations();
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new formation
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addFormation(@RequestBody Map<String, Object> newFormationData) {
        String id = generateRandomString(12);
        String formationName = (String) newFormationData.get("name");
        String formationDescription = (String) newFormationData.get("description");
        FormationType formationType = FormationType.valueOf(((String) newFormationData.get("formation_type")).toUpperCase());

        formationService.addFormation(id, formationName, formationDescription, formationType);
        return ResponseEntity.ok("Formation added successfully!");
    }

    // Endpoint to update an existing formation
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateFormation(@PathVariable String id, @RequestBody Map<String, Object> updatedFormationData) {
        String formationName = (String) updatedFormationData.get("name");
        String formationDescription = (String) updatedFormationData.get("description");
        FormationType formationType = FormationType.valueOf(((String) updatedFormationData.get("formation_type")).toUpperCase());

        formationService.updateFormation(id, formationName, formationDescription, formationType);
        return ResponseEntity.ok("Formation updated successfully!");
    }

    // Endpoint to delete a formation
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFormation(@PathVariable String id) {
        formationService.deleteFormation(id);
        return ResponseEntity.ok("Formation deleted successfully!");
    }

    // Helper method to generate a random string for formation IDs
    private static final String CHARACTERS = "0123456789";
    private static final Random RANDOM = new Random();

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
    // Endpoint to retrieve a formation by ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFormationById(@PathVariable String id) {
        String result = formationService.getFormationById(id); // You need to implement this in the service
        if (result != null) {
            return ResponseEntity.ok(result); // Return the formation if found
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if formation not found
        }
    }

}
