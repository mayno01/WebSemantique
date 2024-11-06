package com.example.webs.Services;

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
public class EventService {

    private static final String RDF_FILE_PATH = "C:\\Users\\hamdi\\Desktop\\webSem.rdf";
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

    // Add a new event
    public void addEvent(String id, String name, String description, String date) {
        if (model == null) {
            loadRDF();
        }

        Resource eventResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        eventResource.addProperty(RDF.type, model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Event"));

        eventResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                name);
        eventResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"),
                description);
        eventResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDate"),
                date);

        saveRDF();
    }

    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update an existing event
    public void updateEvent(String id, String name, String description, String date) {
        if (model == null) {
            loadRDF();
        }

        Resource eventResource = model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (eventResource != null) {
            eventResource.removeAll(model
                    .getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"));
            eventResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                    name);

            eventResource.removeAll(model
                    .getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"));
            eventResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"),
                    description);

            eventResource.removeAll(model
                    .getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDate"));
            eventResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDate"),
                    date);

            saveRDF();
        }
    }

    // Delete an event
    public void deleteEvent(String id) {
        if (model == null) {
            loadRDF();
        }

        Resource eventResource = model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (eventResource != null) {
            model.removeAll(eventResource, null, null);
            model.removeAll(null, null, eventResource);

            saveRDF();
        }
    }

    // Get all events
    public String getEvents() {
        if (model == null) {
            loadRDF();
        }

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?event ?name ?date ?description "
                + "WHERE { "
                + "  ?event a ontology:Event . "
                + "  ?event ontology:hasName ?name . "
                + "  ?event ontology:hasDate ?date . "
                + "  ?event ontology:hasDescription ?description . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray eventsArray = new JSONArray();
            Map<String, JSONObject> eventMap = new HashMap<>();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String eventName = solution.get("name").toString();
                String eventDate = solution.get("date").toString();
                String eventDescription = solution.get("description").toString();
                String idURL = solution.getResource("event").toString();
                String id = idURL.split("#")[1];

                JSONObject eventObject = eventMap.getOrDefault(eventName, new JSONObject());
                if (!eventObject.has("Event")) {
                    eventObject.put("id", id);
                    eventObject.put("Name", eventName);
                    eventObject.put("Date", eventDate);
                    eventObject.put("Description", eventDescription);
                    eventMap.put(eventName, eventObject);
                }
                eventsArray.put(eventObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("Events", eventsArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying events: " + e.getMessage());
        }
    }
}
