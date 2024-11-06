package com.example.webs.Services;


import com.example.webs.Enums.UserRole;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

@Component
public class UserService {
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

    // Register a new user
    public void registerUser(String username, String password, UserRole role) {
        if (model == null) {
            loadRDF();
        }

        String userId = UUID.randomUUID().toString();
        Resource userResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + userId);

        userResource.addProperty(RDF.type, model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#User"));
        userResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#username"), username);
        userResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#password"), password);
        userResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#role"), role.name());

        saveRDF();
    }

    // Login user by validating username and password
    public boolean loginUser(String username, String password) {
        if (model == null) {
            loadRDF();
        }

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?user "
                + "WHERE { "
                + "  ?user a ontology:User . "
                + "  ?user ontology:username ?username . "
                + "  ?user ontology:password ?password . "
                + "  FILTER(?username = \"" + username + "\" && ?password = \"" + password + "\") "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            return results.hasNext();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

            Map<String, String> userData = Map.of(
                    "userId", userId,
                    "username", username,
                    "role", role
            );
            return Optional.of(userData);
        }

        return Optional.empty();
    }


    // Retrieve all users
    public List<Map<String, String>> getAllUsers() {
        if (model == null) {
            loadRDF();
        }

        List<Map<String, String>> users = new ArrayList<>();
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?user ?username ?role "
                + "WHERE { "
                + "  ?user a ontology:User ; "
                + "       ontology:username ?username ; "
                + "       ontology:role ?role . "
                + "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String userId = solution.getResource("user").getLocalName();
                String username = solution.getLiteral("username").getString();
                String role = solution.getLiteral("role").getString();

                users.add(Map.of(
                        "userId", userId,
                        "username", username,
                        "role", role
                ));
            }
        }
        return users;
    }
}
