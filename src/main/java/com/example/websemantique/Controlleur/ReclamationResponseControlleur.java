package com.example.websemantique.Controlleur;


import com.example.websemantique.Enums.TypeReclamation;
import com.example.websemantique.Enums.TypeReponse;
import com.example.websemantique.Services.ReclamationResponse;
import com.example.websemantique.Services.ReclamationResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Enable CORS for this controller

@RequestMapping("/api/reclamations")
public class ReclamationResponseControlleur {

    @Autowired
    private ReclamationResponseService reclamationResponseService;
    @PostMapping(value = "/addReclamation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addReclamation(@RequestBody ReclamationRequest reclamationRequest) {
        try {
            // Calling the service method to add the reclamation
            reclamationResponseService.addReclamation(
                    reclamationRequest.getTitle(),
                    reclamationRequest.getDescription(),
                    reclamationRequest.getDate(),
                    TypeReclamation.valueOf(reclamationRequest.getType())
            );
            return ResponseEntity.ok("Reclamation added successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid reclamation type!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while adding reclamation.");
        }
    }


    // Endpoint pour récupérer toutes les réclamations sous forme de JSON
    @GetMapping(value = "/all",  produces = MediaType.APPLICATION_JSON_VALUE)
    public String getReclamations() {
        try {
            return reclamationResponseService.getReclamations();
        } catch (Exception e) {
            return "Erreur lors de la récupération des réclamations : " + e.getMessage();
        }
    }

    @DeleteMapping( "/delete/{id}")
    public ResponseEntity<String> deleteReclamation(@PathVariable String id) {
        try {
            reclamationResponseService.deleteReclamation(id);
            return ResponseEntity.ok("Réclamation supprimée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la suppression de la réclamation : " + e.getMessage());
        }
    }
    @DeleteMapping("/responses/delete/{responseId}")
    public ResponseEntity<String> deleteResponse(@PathVariable String responseId) {
        try {
            reclamationResponseService.deleteResponse(responseId);
            return ResponseEntity.ok("Réponse supprimée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la suppression de la réponse : " + e.getMessage());
        }
    }
    @PostMapping(value = "/addResponse", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addResponse(@RequestBody ReclamationResponseRequest responseRequest) {
        try {
            // Call service to add the response
            reclamationResponseService.addResponse(
                    responseRequest.getReclamationId(),
                    responseRequest.getResponseId(),
                    responseRequest.getResponseText(),
//                    responseRequest.getDate(),
                    TypeReponse.valueOf(responseRequest.getType())
            );
            return ResponseEntity.ok("Response added successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid response type!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while adding response.");
        }
    }
    @GetMapping(value = "/{reclamationId}/responses" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReclamationResponse> getResponsesForReclamation(@PathVariable("reclamationId") String reclamationId) {
        // Call the service to get the responses for the reclamationId
        return reclamationResponseService.getResponsesForReclamation(reclamationId);
    }
}
