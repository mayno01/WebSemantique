package com.example.webs.Services;

import com.example.webs.Enums.InscriptionType;
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
public class InscriptionService {

    private static final String RDF_FILE_PATH = "C:\\Users\\tarek\\Downloads\\websem.rdf";
    private Model model;

    // Load the RDF file into the model
    public Model loadRDF() {
        model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(RDF_FILE_PATH);
        if (in == null) {
            throw new IllegalArgumentException("File not found: " + RDF_FILE_PATH);
        }
        model.read(in, null);
        return model;
    }

    // Add an inscription to the RDF file with InscriptionType
    public void addInscription(String email, String formationId, InscriptionType inscriptionType) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource inscriptionResource = model.createResource(namespace + "Inscription_" + email + "_" + formationId);
        inscriptionResource.addProperty(RDF.type, model.getResource(namespace + "Inscription"));
        inscriptionResource.addProperty(model.getProperty(namespace + "hasEmail"), email);
        inscriptionResource.addProperty(model.getProperty(namespace + "hasFormation"), model.getResource(namespace + formationId));
        inscriptionResource.addProperty(model.getProperty(namespace + "inscriptionType"), inscriptionType.name());

        // Save the updated model to the RDF file
        saveRDF();
    }

    // Save changes to the RDF file
    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieve all inscriptions as JSON
    public String getInscriptions() {
        loadRDF();

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        String queryString = "PREFIX ontology: <" + namespace + "> "
                + "SELECT ?Inscription ?hasEmail ?hasFormation ?inscriptionType "
                + "WHERE { "
                + "  ?Inscription a ontology:Inscription . "
                + "  ?Inscription ontology:hasEmail ?hasEmail . "
                + "  ?Inscription ontology:hasFormation ?hasFormation . "
                + "  ?Inscription ontology:inscriptionType ?inscriptionType . "
                + "}";

        // Execute the SPARQL query
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            JSONArray jsonArray = new JSONArray();

            // Process results and convert to JSON
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Inscription", soln.getResource("Inscription").toString());
                jsonObject.put("hasEmail", soln.getLiteral("hasEmail").getString());
                jsonObject.put("hasFormation", soln.getResource("hasFormation").toString());
                jsonObject.put("inscriptionType", soln.getLiteral("inscriptionType").getString());
                jsonArray.put(jsonObject);
            }

            // Return JSON array as string
            return jsonArray.toString();
        }
    }

    // Delete an inscription by email and formation ID
    public void deleteInscription(String email, String formationId) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource inscriptionResource = model.getResource(namespace + "Inscription_" + email + "_" + formationId);

        if (inscriptionResource != null) {
            model.removeAll(inscriptionResource, null, null);
            model.removeAll(null, null, inscriptionResource);

            saveRDF();
        }
    }
}
