package com.example.webs.Controllers;
import com.example.webs.Enums.UserRole;
import com.example.webs.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, Object> user) {
        String username = (String) user.get("username");
        String password = (String) user.get("password");
        UserRole role = UserRole.valueOf((String) user.get("role"));

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return ResponseEntity.status(400).body("Username and password cannot be empty.");
        }

        if (username.length() < 4) {
            return ResponseEntity.status(400).body("Username must be at least 4 characters long.");
        }

        if (password.length() < 6) {
            return ResponseEntity.status(400).body("Password must be at least 6 characters long.");
        }

        userService.registerUser(username, password, role);
        return ResponseEntity.ok("User registered successfully!");
    }

    // Update GroupId for a user
    @PutMapping("/updateGroupId/{userId}")
    public ResponseEntity<String> updateUserGroupId(@PathVariable String userId, @RequestBody Map<String, String> groupIdData) {
        String groupId = groupIdData.get("groupId");

        if (groupId == null || groupId.trim().isEmpty()) {
            return ResponseEntity.status(400).body("GroupId cannot be empty.");
        }

        boolean success = userService.updateUserGroupId(userId, groupId);
        if (success) {
            return ResponseEntity.ok("GroupId updated successfully!");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Check if username or password is empty
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return ResponseEntity.status(400).body("Username and password cannot be empty.");
        }

        Optional<String> userIdOpt = userService.loginUser(username, password);
        if (userIdOpt.isPresent()) {
            String userId = userIdOpt.get();  // Retrieve userId
            String token = userService.generateToken(username, userId); // Generate token with userId

            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }
    }



    // Delete user account
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String username) {
        boolean deletionSuccess = userService.deleteUser(username);
        if (deletionSuccess) {
            return ResponseEntity.ok("User deleted successfully!");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getUserById(@PathVariable("id") String userId) {
        Optional<Map<String, String>> userData = userService.getUserById(userId);
        return userData
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(null));
    }
    @PutMapping("/{userId}/group")
    public ResponseEntity<?> updateUserGroup(@PathVariable String userId, @RequestBody Map<String, String> payload) {
        String groupId = payload.get("groupId");
        boolean isUpdated = userService.updateUserGroupId(userId, groupId);

        if (isUpdated) {
            return ResponseEntity.ok("Group updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


    // Retrieve all users
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> getAllUsers() {
        List<Map<String, String>> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
