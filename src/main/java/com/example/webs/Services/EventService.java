package com.example.webs.Services;

import com.example.webs.Enums.EventType;
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

    private static final String RDF_FILE_PATH = "C:\\Users\\Media-pc\\Desktop\\web\\webSem.rdf";
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

    // Add a new event with EventType
    public void addEvent(String id, String name, String description, String date, EventType eventType) {
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

        // Add event type as an enum property
        eventResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasEventType"),
                eventType.name());

        saveRDF();
    }

    // Update an existing event with EventType
    public void updateEvent(String id, String name, String description, String date, EventType eventType) {
        if (model == null) {
            loadRDF();
        }

        // Locate the event by its id (using the URL fragment part after #)
        Resource eventResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (eventResource != null) {
            // Update the event properties
            eventResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"));
            eventResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                    name);

            eventResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"));
            eventResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"),
                    description);

            eventResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDate"));
            eventResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDate"),
                    date);

            eventResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasEventType"));
            eventResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasEventType"),
                    eventType.name());

            saveRDF();
        } else {
            throw new IllegalArgumentException("Event not found with ID: " + id);
        }
    }


    // Get all events
    public String getEvents() {
        if (model == null) {
            loadRDF();
        }

        // Update the query to use the correct property for event type
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?event ?name ?date ?description ?eventType "
                + "WHERE { "
                + "  ?event a ontology:Event . "
                + "  ?event ontology:hasName ?name . "
                + "  ?event ontology:hasDate ?date . "
                + "  ?event ontology:hasDescription ?description . "
                + "  ?event ontology:hasEventType ?eventType . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            // Use a JSONArray to collect the events
            JSONArray eventsArray = new JSONArray();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String eventId = solution.getResource("event").toString().split("#")[1];  // Get the event ID
                String eventName = solution.get("name").toString();
                String eventDate = solution.get("date").toString();
                String eventDescription = solution.get("description").toString();
                String eventType = solution.get("eventType").toString();

                // Create the event object with all necessary details
                JSONObject eventObject = new JSONObject();
                eventObject.put("id", eventId);
                eventObject.put("Name", eventName);
                eventObject.put("Date", eventDate);
                eventObject.put("Description", eventDescription);
                eventObject.put("EventType", eventType);

                // Add eventObject to the array
                eventsArray.put(eventObject);
            }

            // Return the JSON result
            JSONObject resultJson = new JSONObject();
            resultJson.put("Events", eventsArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying events: " + e.getMessage());
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
    // Get an event by its ID
    public String getEventById(String id) {
        if (model == null) {
            loadRDF();
        }

        // Construct the URL for the event resource
        String eventUri = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id;
        Resource eventResource = model.getResource(eventUri);

        if (eventResource == null) {
            throw new IllegalArgumentException("Event not found with ID: " + id);
        }

        // Prepare the query to retrieve details of the event
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?name ?date ?description ?eventType "
                + "WHERE { "
                + "  <" + eventUri + "> ontology:hasName ?name . "
                + "  <" + eventUri + "> ontology:hasDate ?date . "
                + "  <" + eventUri + "> ontology:hasDescription ?description . "
                + "  <" + eventUri + "> ontology:hasEventType ?eventType . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String eventName = solution.get("name").toString();
                String eventDate = solution.get("date").toString();
                String eventDescription = solution.get("description").toString();
                String eventType = solution.get("eventType").toString();

                // Create the JSON response object
                JSONObject eventObject = new JSONObject();
                eventObject.put("id", id);
                eventObject.put("Name", eventName);
                eventObject.put("Date", eventDate);
                eventObject.put("Description", eventDescription);
                eventObject.put("EventType", eventType);

                return eventObject.toString();
            } else {
                throw new IllegalArgumentException("Event not found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying event by ID: " + e.getMessage());
        }
    }


    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Get events by EventType
    public String getEventByEventType(EventType eventType) {
        if (model == null) {
            loadRDF();
        }

        // Update the query to filter by eventType
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?event ?name ?date ?description ?eventType "
                + "WHERE { "
                + "  ?event a ontology:Event . "
                + "  ?event ontology:hasName ?name . "
                + "  ?event ontology:hasDate ?date . "
                + "  ?event ontology:hasDescription ?description . "
                + "  ?event ontology:hasEventType ?eventType . "
                + "  FILTER(?eventType = \"" + eventType.name() + "\")"  // Filtering by event type
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            // Use a JSONArray to collect the filtered events
            JSONArray eventsArray = new JSONArray();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String eventId = solution.getResource("event").toString().split("#")[1];  // Get the event ID
                String eventName = solution.get("name").toString();
                String eventDate = solution.get("date").toString();
                String eventDescription = solution.get("description").toString();
                String eventTypeStr = solution.get("eventType").toString();

                // Create the event object with all necessary details
                JSONObject eventObject = new JSONObject();
                eventObject.put("id", eventId);
                eventObject.put("Name", eventName);
                eventObject.put("Date", eventDate);
                eventObject.put("Description", eventDescription);
                eventObject.put("EventType", eventTypeStr);

                // Add eventObject to the array
                eventsArray.put(eventObject);
            }

            // Return the JSON result
            JSONObject resultJson = new JSONObject();
            resultJson.put("Events", eventsArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
