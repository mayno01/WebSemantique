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
public class BlogService {

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

    // Add a blog to the RDF file
    public void addBlog(String id, String name, String description, String addedDate) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource blogResource = model.createResource(namespace + id);
        blogResource.addProperty(RDF.type, model.getResource(namespace + "Blog"));
        blogResource.addProperty(model.getProperty(namespace + "hasName"), name);
        blogResource.addProperty(model.getProperty(namespace + "hasDescription"), description);
        blogResource.addProperty(model.getProperty(namespace + "hasAdded_Date"), addedDate);

        // Save the updated model to the RDF file
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

    // Update a blog's information
    public void updateBlog(String id, String name, String description, String addedDate) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource blogResource = model.getResource(namespace + id);

        if (blogResource != null) {
            blogResource.removeAll(model.getProperty(namespace + "hasName"));
            blogResource.addProperty(model.getProperty(namespace + "hasName"), name);

            blogResource.removeAll(model.getProperty(namespace + "hasDescription"));
            blogResource.addProperty(model.getProperty(namespace + "hasDescription"), description);

            blogResource.removeAll(model.getProperty(namespace + "hasAdded_Date"));
            blogResource.addProperty(model.getProperty(namespace + "hasAdded_Date"), addedDate);

            saveRDF();
        }
    }

    // Delete a blog by its ID
    public void deleteBlog(String id) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource blogResource = model.getResource(namespace + id);

        if (blogResource != null) {
            model.removeAll(blogResource, null, null);
            model.removeAll(null, null, blogResource);

            saveRDF();
        }
    }

    // Retrieve all blogs as JSON
    public String getBlogs() {
        loadRDF();

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        String queryString = "PREFIX ontology: <" + namespace + "> "
                + "SELECT ?Blog ?hasName ?hasAdded_Date ?hasDescription "
                + "WHERE { "
                + "  ?Blog a ontology:Blog . "
                + "  ?Blog ontology:hasName ?hasName . "
                + "  ?Blog ontology:hasAdded_Date ?hasAdded_Date . "
                + "  ?Blog ontology:hasDescription ?hasDescription . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            JSONArray blogsArray = new JSONArray();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                JSONObject blogObject = new JSONObject();
                String idURL = solution.getResource("Blog").toString();
                String id = idURL.split("#")[1];

                blogObject.put("id", id);
                blogObject.put("Name", solution.getLiteral("hasName").getString());
                blogObject.put("Added_Date", solution.getLiteral("hasAdded_Date").getString());
                blogObject.put("Description", solution.getLiteral("hasDescription").getString());

                blogsArray.put(blogObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("Blogs", blogsArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying blogs: " + e.getMessage());
        }
    }
}
