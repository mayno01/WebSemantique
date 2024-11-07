package com.example.websemantique.Services;


import com.example.websemantique.Enums.TypeReclamation;
import com.example.websemantique.Enums.TypeReponse;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Component;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.UUID;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReclamationResponseService {

    private static final String RDF_FILE_PATH = "C:\\Users\\skand\\Downloads\\websem.rdf";
    private Model model;

    // Charger le fichier RDF
    public Model loadRDF() {
        model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(RDF_FILE_PATH);
        if (in == null) {
            throw new IllegalArgumentException("Fichier introuvable: " + RDF_FILE_PATH);
        }
        model.read(in, null);
        return model;
    }

    // Ajouter une réclamation
    public void addReclamation(String title, String description, String date, TypeReclamation type) {
        if (model == null) {
            loadRDF();
        }

        String id = UUID.randomUUID().toString(); // Generate a unique ID

        Resource reclamationResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#" + id);
        reclamationResource.addProperty(RDF.type, model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#Reclamation"));

        reclamationResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasTitle"),
                title);
        reclamationResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasDescription"),
                description);
        reclamationResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasDate"),
                date);
        reclamationResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasType"),
                type.name());

        saveRDF();
    }

    // Ajouter une réponse à une réclamation
    // Ajouter une réponse à une réclamation

    public void addResponse(String reclamationId, String responseText, String date, TypeReponse type) {
        if (model == null) {
            loadRDF();
        }

        // Generate a new UUID for the response ID if it's not provided
        String responseId = UUID.randomUUID().toString();  // This will generate a unique ID for the response

        // Create the response resource using the generated responseId
        Resource responseResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#" + responseId);
        responseResource.addProperty(RDF.type, model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#Response"));

        responseResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasResponseText"),
                responseText);
        responseResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasDate"),
                date);
        responseResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasType"),
                type.name()); // Store the type of the response

        // Link the response to the reclamation
        Resource reclamationResource = model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#" + reclamationId);
        reclamationResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasResponse"),
                responseResource);

        saveRDF();
    }

    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Récupérer toutes les réclamations
    public String getReclamations() {
        if (model == null) {
            loadRDF();
        }

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#> "
                + "SELECT ?reclamation ?title ?date ?description "
                + "WHERE { "
                + "  ?reclamation a ontology:Reclamation . "
                + "  ?reclamation ontology:hasTitle ?title . "
                + "  ?reclamation ontology:hasDate ?date . "
                + "  ?reclamation ontology:hasDescription ?description ." +
                "  ?reclamation ontology:hasType ?type . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray reclamationsArray = new JSONArray();
            Map<String, JSONObject> reclamationMap = new HashMap<>();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String reclamationTitle = solution.get("title").toString();
                String reclamationDate = solution.get("date").toString();
                String reclamationDescription = solution.get("description").toString();
                String idURL = solution.getResource("reclamation").toString();
                String id = idURL.split("#")[1];

                JSONObject reclamationObject = reclamationMap.getOrDefault(reclamationTitle, new JSONObject());
                if (!reclamationObject.has("Reclamation")) {
                    reclamationObject.put("id", id);
                    reclamationObject.put("Title", reclamationTitle);
                    reclamationObject.put("Date", reclamationDate);
                    reclamationObject.put("Description", reclamationDescription);
                    reclamationMap.put(reclamationTitle, reclamationObject);
                }
                reclamationsArray.put(reclamationObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("Reclamations", reclamationsArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des réclamations : " + e.getMessage());
        }
    }

    public List<ReclamationResponse> getResponsesForReclamation(String reclamationId) {
        List<ReclamationResponse> responses = new ArrayList<>();

        if (model == null) {
            loadRDF(); // Load RDF data if not already loaded
        }

        // Define the SPARQL query to get all responses for a specific reclamationId
        String sparqlQueryString =
                "PREFIX r: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#> " +
                        "SELECT ?response ?responseText ?date ?type " +
                        "WHERE { " +
                        "  ?reclamation r:hasResponse ?response . " +
                        "  ?response r:hasResponseText ?responseText . " +
                        "  ?response r:hasDate ?date . " +
                        "  ?response r:hasType ?type . " +
                        "  FILTER(?reclamation = r:" + reclamationId + ") " +
                        "}";

        // Execute the SPARQL query on the model
        Query query = QueryFactory.create(sparqlQueryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();

        // Process the results and map them to ReclamationResponse objects
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            String responseText = solution.getLiteral("responseText").getString();
            String date = solution.getLiteral("date").getString();
            String type = solution.getLiteral("type").getString();

            // Create a new ReclamationResponse object and add it to the list
            ReclamationResponse response = new ReclamationResponse();
            response.setResponseText(responseText);
            response.setDate(date);
            response.setType(TypeReponse.valueOf(type)); // Assuming TypeReponse is an enum

            responses.add(response);
        }

        return responses;
    }
    // Mettre à jour une réclamation
    public void updateReclamation(String id, String newTitle, String newDescription, String newDate) {
        if (model == null) {
            loadRDF();
        }

        Resource reclamationResource = model.getResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#" + id);

        if (reclamationResource != null) {
            // Met à jour les propriétés
            reclamationResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasTitle"))
                    .addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasTitle"), newTitle);
            reclamationResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasDescription"))
                    .addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasDescription"), newDescription);
            reclamationResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasDate"))
                    .addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#hasDate"), newDate);

            saveRDF();
        } else {
            throw new IllegalArgumentException("Réclamation introuvable avec l'ID : " + id);
        }
    }

    // Supprimer une réclamation (et ses réponses associées)
    public void deleteReclamation(String id) {
        if (model == null) {
            loadRDF();
        }

        Resource reclamationResource = model.getResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#" + id);

        if (reclamationResource != null) {
            // Supprimer toutes les propriétés et le noeud lui-même
            reclamationResource.removeAll(null);
            model.removeAll(reclamationResource, null, null);
            saveRDF();
        } else {
            throw new IllegalArgumentException("Réclamation introuvable avec l'ID : " + id);
        }
    }

    // Supprimer une réponse associée à une réclamation
    public void deleteResponse(String responseId) {
        if (model == null) {
            loadRDF();
        }

        Resource responseResource = model.getResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/reclamation-response#" + responseId);

        if (responseResource != null) {
            // Supprimer toutes les propriétés et le noeud lui-même
            responseResource.removeAll(null);
            model.removeAll(responseResource, null, null);
            saveRDF();
        } else {
            throw new IllegalArgumentException("Réponse introuvable avec l'ID : " + responseId);
        }
    }

    // Sauvegarder le fichier RDF
}
