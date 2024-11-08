package com.example.webs.Services;

import com.example.webs.Enums.EquipeType;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.InputStream;

@Component
public class EquipeService {

    private static final String RDF_FILE_PATH = "D:\\Downloads\\webSem.rdf";;
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

    public void addEquipe(String id, String type) {
        if (model == null) {
            loadRDF();
        }

        EquipeType equipeType;
        try {
            equipeType = EquipeType.valueOf(type.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid EquipeType provided");
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource equipeResource = model.createResource(namespace + id);
        equipeResource.addProperty(RDF.type, model.getResource(namespace + "Equipe"));
        equipeResource.addProperty(model.getProperty(namespace + "Equipe_type"), equipeType.toString());

        saveRDF();
    }

    public void updateEquipe(String id, String type) {
        if (model == null) {
            loadRDF();
        }

        EquipeType equipeType;
        try {
            equipeType = EquipeType.valueOf(type.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid EquipeType provided");
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource equipeResource = model.getResource(namespace + id);

        if (equipeResource != null) {
            equipeResource.removeAll(model.getProperty(namespace + "Equipe_type"));
            equipeResource.addProperty(model.getProperty(namespace + "Equipe_type"), equipeType.toString());

            saveRDF();
        }
    }

    public void deleteEquipe(String id) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource equipeResource = model.getResource(namespace + id);

        if (equipeResource != null) {
            model.removeAll(equipeResource, null, null);
            model.removeAll(null, null, equipeResource);

            saveRDF();
        }
    }

    public String getEquipes() {
        loadRDF();

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        String queryString = "PREFIX ontology: <" + namespace + "> "
                + "SELECT ?Equipe ?Equipe_type "
                + "WHERE { "
                + "  ?Equipe a ontology:Equipe . "
                + "  ?Equipe ontology:Equipe_type ?Equipe_type . "
                + "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            StringBuilder resultJson = new StringBuilder("[");

            // Flag to check if any results are found
            boolean hasResults = false;

            while (results.hasNext()) {
                hasResults = true; // Set flag to true if there's at least one result
                QuerySolution soln = results.nextSolution();
                String idURL = soln.getResource("Equipe").toString();
                String id = idURL.split("#")[1];
                String type = soln.getLiteral("Equipe_type").getString();
                resultJson.append("{\"id\":\"").append(id).append("\", \"type\":\"").append(type).append("\"},");
            }

            // Only delete the last comma if results were added
            if (hasResults) {
                resultJson.deleteCharAt(resultJson.length() - 1);
            }

            resultJson.append("]");
            return resultJson.toString(); // Returns "[]" if no results
        }
    }

    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
