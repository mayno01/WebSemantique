package com.example.webs.Controllers;

import com.example.webs.Enums.BlogType;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllBlogs() {
        String result = blogService.getBlogs();
        return ResponseEntity.ok(result);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addBlog(@RequestBody Map<String, Object> newBlogData) {
        String id = generateRandomString(12);
        String title = (String) newBlogData.get("title");
        String content = (String) newBlogData.get("content");
        String date = (String) newBlogData.get("date");
        BlogType type = BlogType.valueOf((String) newBlogData.get("type"));

        blogService.addBlog(id, title, content, date, type);
        return ResponseEntity.ok("Blog added successfully!");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBlog(@PathVariable String id, @RequestBody Map<String, Object> updatedBlogData) {
        String title = (String) updatedBlogData.get("title");
        String content = (String) updatedBlogData.get("content");
        String date = (String) updatedBlogData.get("date");
        BlogType type = BlogType.valueOf((String) updatedBlogData.get("type"));

        blogService.updateBlog(id, title, content, date, type);
        return ResponseEntity.ok("Blog updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok("Blog deleted successfully!");
    }

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
