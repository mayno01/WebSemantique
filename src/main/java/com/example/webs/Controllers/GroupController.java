package com.example.webs.Controllers;

import com.example.webs.Services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Enable CORS for this controller
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    // Endpoint to get all groups from the RDF file in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllGroups() {
        String result = groupService.getGroups();
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new group
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addGroup(@RequestBody Map<String, Object> newGroup) {
        String id = generateRandomString(12);
        String groupName = (String) newGroup.get("Name");
        String groupDescription = (String) newGroup.get("Description");

        groupService.addGroup(id, groupName, groupDescription);
        return ResponseEntity.ok("Group added successfully!");
    }

    // Endpoint to update an existing group
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateGroup(@PathVariable String id, @RequestBody Map<String, Object> updatedGroup) {
        String groupName = (String) updatedGroup.get("Name");
        String groupDescription = (String) updatedGroup.get("Description");

        groupService.updateGroup(id, groupName, groupDescription);
        return ResponseEntity.ok("Group updated successfully!");
    }

    // Endpoint to delete a group by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable String id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok("Group deleted successfully!");
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
