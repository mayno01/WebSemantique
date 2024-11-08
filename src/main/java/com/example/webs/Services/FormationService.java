package com.example.webs.Services;

import com.example.webs.Enums.FormationType;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.InputStream;

@Component
public class FormationService {

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

    // Add a formation to the RDF file
    public void addFormation(String id, String name, String description, FormationType formationType) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource formationResource = model.createResource(namespace + id);
        formationResource.addProperty(RDF.type, model.getResource(namespace + "Formation"));
        formationResource.addProperty(model.getProperty(namespace + "hasName"), name);
        formationResource.addProperty(model.getProperty(namespace + "hasDescription"), description);
        formationResource.addProperty(model.getProperty(namespace + "hasFormationType"), formationType.name());

        // Save the updated model to the RDF file
        saveRDF();
    }

    // Update a formation's information
    public void updateFormation(String id, String name, String description, FormationType formationType) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource formationResource = model.getResource(namespace + id);

        if (formationResource != null) {
            formationResource.removeAll(model.getProperty(namespace + "hasName"));
            formationResource.addProperty(model.getProperty(namespace + "hasName"), name);

            formationResource.removeAll(model.getProperty(namespace + "hasDescription"));
            formationResource.addProperty(model.getProperty(namespace + "hasDescription"), description);

            formationResource.removeAll(model.getProperty(namespace + "hasFormationType"));
            formationResource.addProperty(model.getProperty(namespace + "hasFormationType"), formationType.name());

            saveRDF();
        }
    }

    // Delete a formation by its ID
    public void deleteFormation(String id) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource formationResource = model.getResource(namespace + id);

        if (formationResource != null) {
            model.removeAll(formationResource, null, null);
            model.removeAll(null, null, formationResource);

            saveRDF();
        }
    }

    // Retrieve all formations as JSON
    public String getFormations() {
        loadRDF();

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        String queryString = "PREFIX ontology: <" + namespace + "> "
                + "SELECT ?Formation ?hasName ?hasDescription ?hasFormationType "
                + "WHERE { "
                + "  ?Formation a ontology:Formation . "
                + "  ?Formation ontology:hasName ?hasName . "
                + "  ?Formation ontology:hasDescription ?hasDescription . "
                + "  ?Formation ontology:hasFormationType ?hasFormationType . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            JSONArray formationsArray = new JSONArray();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                JSONObject formationObject = new JSONObject();
                String idURL = solution.getResource("Formation").toString();
                String id = idURL.split("#")[1];

                formationObject.put("id", id);
                formationObject.put("name", solution.getLiteral("hasName").getString());
                formationObject.put("description", solution.getLiteral("hasDescription").getString());
                formationObject.put("formation_type", solution.getLiteral("hasFormationType").getString());

                formationsArray.put(formationObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("Formations", formationsArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying formations: " + e.getMessage());
        }
    }

    // Save changes to RDF
    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Retrieve a formation by its ID
    public String getFormationById(String id) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource formationResource = model.getResource(namespace + id);

        if (formationResource != null) {
            // Retrieve the details of the formation and return as JSON
            JSONObject formationObject = new JSONObject();
            formationObject.put("id", id);
            formationObject.put("name", formationResource.getProperty(model.getProperty(namespace + "hasName")).getString());
            formationObject.put("description", formationResource.getProperty(model.getProperty(namespace + "hasDescription")).getString());
            formationObject.put("formation_type", formationResource.getProperty(model.getProperty(namespace + "hasFormationType")).getString());

            return formationObject.toString();
        } else {
            // If no formation found with the given ID
            return null;
        }
    }

}
