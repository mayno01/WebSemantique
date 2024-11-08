package com.example.webs.Controllers;

import com.example.webs.Enums.InscriptionType;
import com.example.webs.Services.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Enable CORS for this controller
@RequestMapping("/inscriptions")
public class InscriptionController {

    @Autowired
    private InscriptionService inscriptionService;

    // Endpoint to add a new inscription with InscriptionType
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addInscription(@RequestBody Map<String, Object> inscriptionData) {
        String email = (String) inscriptionData.get("email");
        String formationId = (String) inscriptionData.get("formationId");
        InscriptionType inscriptionType;

        try {
            // Handle enum conversion with proper validation
            inscriptionType = InscriptionType.valueOf((String) inscriptionData.get("inscriptionType"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid inscription type provided.");
        }

        inscriptionService.addInscription(email, formationId, inscriptionType);
        return ResponseEntity.ok("Inscription added successfully!");
    }

    // Endpoint to retrieve all inscriptions
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllInscriptions() {
        String result = inscriptionService.getInscriptions();
        return ResponseEntity.ok(result);
    }

    // Endpoint to delete an inscription by email and formation ID
    @DeleteMapping("/{email}/{formationId}")
    public ResponseEntity<String> deleteInscription(@PathVariable String email, @PathVariable String formationId) {
        inscriptionService.deleteInscription(email, formationId);
        return ResponseEntity.ok("Inscription deleted successfully!");
    }
}
