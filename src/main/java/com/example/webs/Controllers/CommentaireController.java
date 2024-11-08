package com.example.webs.Controllers;

import com.example.webs.Services.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/commentaires")
public class CommentaireController {

    @Autowired
    private CommentaireService commentaireService;

    // Endpoint to retrieve all commentaires for a specific blog
    @GetMapping(value = "/{blogId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCommentairesByBlog(@PathVariable String blogId) {
        String result = commentaireService.getCommentairesByBlog(blogId);
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new commentaire to a blog
    @PostMapping(value = "/{blogId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addCommentaire(@PathVariable String blogId, @RequestBody Map<String, Object> newCommentaireData) {
        String commentaireId = generateRandomString(12);
        String content = (String) newCommentaireData.get("content");
        String date = (String) newCommentaireData.get("date");

        commentaireService.addCommentaire(commentaireId, blogId, content, date);
        return ResponseEntity.ok("Commentaire added successfully!");
    }

    // Endpoint to delete a commentaire
    @DeleteMapping("/{commentaireId}")
    public ResponseEntity<String> deleteCommentaire(@PathVariable String commentaireId) {
        commentaireService.deleteCommentaire(commentaireId);
        return ResponseEntity.ok("Commentaire deleted successfully!");
    }

    // Helper method to generate a random string for commentaire IDs
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
}
