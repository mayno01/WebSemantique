package com.example.webs.Controllers;

import com.example.webs.Services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Enable CORS for this controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    // Endpoint to retrieve all blogs in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllBlogs() {
        String result = blogService.getBlogs();
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new blog
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addBlog(@RequestBody Map<String, Object> newBlogData) {
        String id = generateRandomString(12);
        String blogName = (String) newBlogData.get("Name");
        String blogDescription = (String) newBlogData.get("Description");
        String addedDate = (String) newBlogData.get("Added_Date");

        blogService.addBlog(id, blogName, blogDescription, addedDate);
        return ResponseEntity.ok("Blog added successfully!");
    }

    // Endpoint to update an existing blog
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBlog(@PathVariable String id, @RequestBody Map<String, Object> updatedBlogData) {
        String blogName = (String) updatedBlogData.get("Name");
        String blogDescription = (String) updatedBlogData.get("Description");
        String addedDate = (String) updatedBlogData.get("Added_Date");

        blogService.updateBlog(id, blogName, blogDescription, addedDate);
        return ResponseEntity.ok("Blog updated successfully!");
    }

    // Endpoint to delete a blog
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok("Blog deleted successfully!");
    }

    // Helper method to generate a random string for blog IDs
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
