package com.example.webs.Services;

import com.example.webs.Enums.GroupType;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class GroupService {

    private static final String RDF_FILE_PATH = "D:\\Downloads\\webSem.rdf";
    private Model model;

    public Model loadRDF() {
        model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(RDF_FILE_PATH);
        if (in == null) {
            throw new IllegalArgumentException("File not found: " + RDF_FILE_PATH);
        }
        model.read(in, null);
        return model;
    }

    // Add a new group
    public void addGroup(String id, String name, String description) {
        if (model == null) {
            loadRDF();
        }

        // Validate group name is in GroupType enum
        try {
            GroupType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid group name: " + name);
        }

        Resource groupResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        groupResource.addProperty(RDF.type, model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Group"));

        groupResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                name);
        groupResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"),
                description);

        saveRDF();
    }

    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update an existing group
    public void updateGroup(String id, String name, String description) {
        if (model == null) {
            loadRDF();
        }

        // Validate group name is in GroupType enum
        try {
            GroupType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid group name: " + name);
        }

        Resource groupResource = model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (groupResource != null) {
            groupResource.removeAll(model
                    .getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"));
            groupResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                    name);

            groupResource.removeAll(model
                    .getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"));
            groupResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"),
                    description);

            saveRDF();
        }
    }

    // Delete a group
    public void deleteGroup(String id) {
        if (model == null) {
            loadRDF();
        }

        Resource groupResource = model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (groupResource != null) {
            model.removeAll(groupResource, null, null);
            model.removeAll(null, null, groupResource);

            saveRDF();
        }
    }

    // Get all groups
    public String getGroups() {
        if (model == null) {
            loadRDF();
        }

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?group ?name ?description "
                + "WHERE { "
                + "  ?group a ontology:Group . "
                + "  ?group ontology:hasName ?name . "
                + "  ?group ontology:hasDescription ?description . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray groupsArray = new JSONArray();
            Map<String, JSONObject> groupMap = new HashMap<>();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String groupName = solution.get("name").toString();
                String groupDescription = solution.get("description").toString();
                String idURL = solution.getResource("group").toString();
                String id = idURL.split("#")[1];

                JSONObject groupObject = groupMap.getOrDefault(groupName, new JSONObject());
                if (!groupObject.has("Group")) {
                    groupObject.put("id", id);
                    groupObject.put("Name", groupName);
                    groupObject.put("Description", groupDescription);
                    groupMap.put(groupName, groupObject);
                }
                groupsArray.put(groupObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("Groups", groupsArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying groups: " + e.getMessage());
        }
    }
}
