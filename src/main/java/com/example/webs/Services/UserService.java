package com.example.webs.Services;

import io.jsonwebtoken.security.Keys;
import com.example.webs.Enums.UserRole;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Component
public class UserService {
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String RDF_FILE_PATH = "D:\\Downloads\\webSem.rdf";
    private Model model;

    // Load RDF model from file
    public Model loadRDF() {
        model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(RDF_FILE_PATH);
        if (in == null) {
            throw new IllegalArgumentException("File not found: " + RDF_FILE_PATH);
        }
        model.read(in, null);
        return model;
    }


    public void registerUser(String username, String password, UserRole role) {
        String hashedPassword = passwordEncoder.encode(password);
        if (model == null) {
            loadRDF();
        }

        String userId = UUID.randomUUID().toString();
        Resource userResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + userId);

        userResource.addProperty(RDF.type, model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#User"));
        userResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#username"), username);
        userResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#password"), hashedPassword);
        userResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#role"), role.name());

        // Set an empty GroupId for the newly registered user
        userResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#groupId"), "152033818458");

        saveRDF();
    }
    // Method to update GroupId after registration
    public boolean updateUserGroupId(String userId, String groupId) {
        if (model == null) {
            loadRDF();
        }

        String userUri = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + userId;
        Resource userResource = model.getResource(userUri);

        if (userResource != null) {
            userResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#groupId"));
            userResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#groupId"), groupId); // Use ID here
            saveRDF();
            return true;
        }
        return false;
    }



    // Login user by validating username and password
    public Optional<String> loginUser(String username, String password) {
        if (model == null) {
            loadRDF();
        }

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?user ?storedPassword "
                + "WHERE { "
                + "  ?user a ontology:User . "
                + "  ?user ontology:username ?username . "
                + "  ?user ontology:password ?storedPassword . "
                + "  FILTER(?username = \"" + username + "\") "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String storedPassword = solution.getLiteral("storedPassword").getString();
                String userId = solution.getResource("user").getLocalName(); // Extract userId

                if (passwordEncoder.matches(password, storedPassword)) {
                    return Optional.of(userId); // Return userId on successful login
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty(); // Return empty if login fails
    }

    // Delete a user account by username
    public boolean deleteUser(String username) {
        if (model == null) {
            loadRDF();
        }

        String userUri = findUserUri(username);
        if (userUri != null) {
            Resource userResource = model.getResource(userUri);
            model.removeAll(userResource, null, null);
            model.removeAll(null, null, userResource);
            saveRDF();
            return true;
        }
        return false;
    }

    // Find user URI by username
    private String findUserUri(String username) {
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?user "
                + "WHERE { "
                + "  ?user a ontology:User . "
                + "  ?user ontology:username ?username . "
                + "  FILTER(?username = \"" + username + "\") "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                return solution.getResource("user").getURI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Save changes to RDF file
    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Retrieve user by ID
    // Retrieve user by ID
    public Optional<Map<String, String>> getUserById(String userId) {
        if (model == null) {
            loadRDF();
        }

        // Create the full URI of the user based on the provided userId
        String userUri = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + userId;
        Resource userResource = model.getResource(userUri);

        if (userResource != null && userResource.hasProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#username"))) {
            String username = userResource.getProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#username")).getString();
            String role = userResource.getProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#role")).getString();

            // Fetch groupId
            String groupId = userResource.getProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#groupId")).getString();

            // Retrieve the group name based on groupId
            Optional<String> groupNameOpt = getGroupNameById(groupId);
            String groupName = groupNameOpt.orElse("No Group");

            Map<String, String> userData = Map.of(
                    "userId", userId,
                    "username", username,
                    "role", role,
                    "groupId", groupId,
                    "groupName", groupName
            );
            return Optional.of(userData);
        }

        return Optional.empty();
    }


    public List<Map<String, String>> getAllUsers() {
        if (model == null) {
            loadRDF();
        }

        List<Map<String, String>> users = new ArrayList<>();
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?user ?username ?role ?groupId "
                + "WHERE { "
                + "  ?user a ontology:User ; "
                + "       ontology:username ?username ; "
                + "       ontology:role ?role ; "
                + "       ontology:groupId ?groupId . "
                + "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String userId = solution.getResource("user").getLocalName();
                String username = solution.getLiteral("username").getString();
                String role = solution.getLiteral("role").getString();
                String groupId = solution.getLiteral("groupId").getString();

                // Retrieve the group name
                Optional<String> groupNameOpt = getGroupNameById(groupId);
                String groupName = groupNameOpt.orElse("No Group");

                users.add(Map.of(
                        "userId", userId,
                        "username", username,
                        "role", role,
                        "groupName", groupName
                ));
            }
        }
        return users;
    }




    public String generateToken(String username,String userId) {
        String SECRET_KEY = "WebSemantique123123AABFAC9985254AABBBWebSemantique123123AABFAC9985254AABBB";

        long expirationTime = 1000 * 60 * 60 * 24;
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public Optional<String> getGroupNameById(String groupId) {
        if (model == null) {
            loadRDF();
        }

        String queryString = "PREFIX j0: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?hasName "
                + "WHERE { "
                + "  j0:" + groupId + " a j0:Group ; "
                + "       j0:hasName ?hasName . "
                + "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                return Optional.of(solution.getLiteral("hasName").getString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    private String getGroupsNameById(String groupId) {
        // Implement this lookup based on your existing data or a predefined mapping of group IDs to names.
        Map<String, String> groupMap = Map.of(
                "901366217470", "BASIC",
                "937488306806", "VIP",
                "530828668616", "PREMIUM"
        );
        return groupMap.getOrDefault(groupId, "Unknown Group");
    }

}
