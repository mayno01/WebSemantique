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

@Component
public class CommentaireService {

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

    // Add a commentaire to a blog
    public void addCommentaire(String commentaireId, String blogId, String content, String date) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource commentaireResource = model.createResource(namespace + commentaireId);
        commentaireResource.addProperty(RDF.type, model.getResource(namespace + "Commentaire"));
        commentaireResource.addProperty(model.getProperty(namespace + "commentaire_content"), content);
        commentaireResource.addProperty(model.getProperty(namespace + "commentaire_date"), date);

        // Associate commentaire with blog
        Resource blogResource = model.getResource(namespace + blogId);
        if (blogResource != null) {
            commentaireResource.addProperty(model.getProperty(namespace + "isCommentOn"), blogResource);
        }

        saveRDF();
    }

    // Save changes to RDF
    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieve all commentaires for a blog as JSON
    public String getCommentairesByBlog(String blogId) {
        loadRDF();

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        String queryString = "PREFIX ontology: <" + namespace + "> "
                + "SELECT ?Commentaire ?commentaire_content ?commentaire_date "
                + "WHERE { "
                + "  ?Commentaire a ontology:Commentaire . "
                + "  ?Commentaire ontology:commentaire_content ?commentaire_content . "
                + "  ?Commentaire ontology:commentaire_date ?commentaire_date . "
                + "  ?Commentaire ontology:isCommentOn <" + namespace + blogId + "> . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            JSONArray commentairesArray = new JSONArray();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                JSONObject commentaireObject = new JSONObject();
                String idURL = solution.getResource("Commentaire").toString();
                String commentaireId = idURL.split("#")[1];

                commentaireObject.put("commentaire_id", commentaireId);
                commentaireObject.put("content", solution.getLiteral("commentaire_content").getString());
                commentaireObject.put("date", solution.getLiteral("commentaire_date").getString());

                commentairesArray.put(commentaireObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("Commentaires", commentairesArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying commentaires: " + e.getMessage());
        }
    }

    // Delete a commentaire by its ID
    public void deleteCommentaire(String commentaireId) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource commentaireResource = model.getResource(namespace + commentaireId);

        if (commentaireResource != null) {
            model.removeAll(commentaireResource, null, null);
            model.removeAll(null, null, commentaireResource);

            saveRDF();
        }
    }
}

