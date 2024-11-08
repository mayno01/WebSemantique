package com.example.webs.Controllers;

import com.example.webs.Services.EquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Adjust CORS settings as needed
@RequestMapping("/equipes")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    // Retrieve all equipes
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllEquipes() {
        String result = equipeService.getEquipes();
        return ResponseEntity.ok(result);
    }

    // Add a new equipe
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addEquipe(@RequestBody Map<String, Object> newEquipeData) {
        String equipeId = (String) newEquipeData.get("Equipe_id");
        String equipeType = (String) newEquipeData.get("Equipe_type");

        try {
            equipeService.addEquipe(equipeId, equipeType);
            return ResponseEntity.ok("Equipe added successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Invalid Equipe type provided");
        }
    }

    // Update an existing equipe
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEquipe(@PathVariable String id, @RequestBody Map<String, Object> updatedEquipeData) {
        String equipeType = (String) updatedEquipeData.get("Equipe_type");

        try {
            equipeService.updateEquipe(id, equipeType);
            return ResponseEntity.ok("Equipe updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Invalid Equipe type provided");
        }
    }

    // Delete an equipe
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEquipe(@PathVariable String id) {
        equipeService.deleteEquipe(id);
        return ResponseEntity.ok("Equipe deleted successfully!");
    }
}
