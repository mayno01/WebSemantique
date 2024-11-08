package com.example.webs.Services;

import com.example.webs.Enums.ParticipationType;
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
import java.util.UUID;
@Component
public class ParticipationService {

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

    // Check if an event exists by ID
    public boolean eventExists(String eventId) {
        if (model == null) {
            loadRDF();
        }
        String eventUri = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + eventId;
        Resource eventResource = model.getResource(eventUri);
        return eventResource != null && eventResource.hasProperty(RDF.type, model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Event"));
    }

    public void addParticipation(String participantName, String eventId, ParticipationType participationType) {
        if (model == null) {
            loadRDF();
        }

        if (!eventExists(eventId)) {
            throw new IllegalArgumentException("Event with ID " + eventId + " does not exist.");
        }

        // Generate a unique ID automatically
        String id = UUID.randomUUID().toString();

        Resource participationResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        participationResource.addProperty(RDF.type, model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Participation"));

        participationResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasParticipantName"),
                participantName);
        participationResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#participationType"),
                participationType.name());

        // Link participation to the event
        participationResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#isParticipationOf"),
                model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + eventId));

        saveRDF();
    }
    // Save RDF file
    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieve all participations
    public String getParticipations() {
        if (model == null) {
            loadRDF();
        }

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?participation ?participantName ?participationType ?event "
                + "WHERE { "
                + "  ?participation a ontology:Participation . "
                + "  ?participation ontology:hasParticipantName ?participantName . "
                + "  ?participation ontology:participationType ?participationType . "
                + "  ?participation ontology:isParticipationOf ?event . "
                + "}";

        Query query = QueryFactory.create(queryString);
        JSONArray participationsArray = new JSONArray();

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String participationId = solution.getResource("participation").toString().split("#")[1];
                String participantName = solution.get("participantName").toString();
                String participationType = solution.get("participationType").toString();
                String eventId = solution.getResource("event").toString().split("#")[1];

                JSONObject participationObject = new JSONObject();
                participationObject.put("id", participationId);
                participationObject.put("participantName", participantName);
                participationObject.put("participationType", participationType);
                participationObject.put("eventId", eventId);

                participationsArray.put(participationObject);
            }
        }
        JSONObject resultJson = new JSONObject();
        resultJson.put("Participations", participationsArray);
        return resultJson.toString();
    }

    // Delete a participation
    public void deleteParticipation(String id) {
        if (model == null) {
            loadRDF();
        }

        Resource participationResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (participationResource != null) {
            model.removeAll(participationResource, null, null);
            model.removeAll(null, null, participationResource);

            saveRDF();
        }
    }

    // Method to retrieve participations by event ID
    public String getParticipationByEvent(String eventId) {
        if (model == null) {
            loadRDF();
        }

        String eventUri = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + eventId;

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?participation ?participantName ?participationType "
                + "WHERE { "
                + "  ?participation a ontology:Participation . "
                + "  ?participation ontology:hasParticipantName ?participantName . "
                + "  ?participation ontology:participationType ?participationType . "
                + "  ?participation ontology:isParticipationOf <" + eventUri + "> . "
                + "}";

        Query query = QueryFactory.create(queryString);
        JSONArray participationsArray = new JSONArray();

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String participationId = solution.getResource("participation").toString().split("#")[1];
                String participantName = solution.get("participantName").toString();
                String participationType = solution.get("participationType").toString();

                JSONObject participationObject = new JSONObject();
                participationObject.put("id", participationId);
                participationObject.put("participantName", participantName);
                participationObject.put("participationType", participationType);

                participationsArray.put(participationObject);
            }
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("Participations", participationsArray);
        return resultJson.toString();
    }
    // Method to retrieve participations grouped by event ID
    public String getParticipationsByAllEvents() {
        if (model == null) {
            loadRDF();
        }

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?event ?participation ?participantName ?participationType "
                + "WHERE { "
                + "  ?participation a ontology:Participation . "
                + "  ?participation ontology:hasParticipantName ?participantName . "
                + "  ?participation ontology:participationType ?participationType . "
                + "  ?participation ontology:isParticipationOf ?event . "
                + "}";

        Query query = QueryFactory.create(queryString);
        JSONObject eventsWithParticipations = new JSONObject();

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String eventId = solution.getResource("event").toString().split("#")[1];
                String participationId = solution.getResource("participation").toString().split("#")[1];
                String participantName = solution.get("participantName").toString();
                String participationType = solution.get("participationType").toString();

                JSONObject participationObject = new JSONObject();
                participationObject.put("id", participationId);
                participationObject.put("participantName", participantName);
                participationObject.put("participationType", participationType);

                // Group participations under each event
                if (!eventsWithParticipations.has(eventId)) {
                    eventsWithParticipations.put(eventId, new JSONArray());
                }
                eventsWithParticipations.getJSONArray(eventId).put(participationObject);
            }
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("Events", eventsWithParticipations);
        return resultJson.toString();
    }

}
