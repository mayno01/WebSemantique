package com.example.webs.Services;

import com.example.webs.Enums.BlogType;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

@Service
public class BlogService {

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

    public void addBlog(String id, String title, String content, String date, BlogType type) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource blogResource = model.createResource(namespace + id);
        blogResource.addProperty(RDF.type, model.getResource(namespace + "Blog"));
        blogResource.addProperty(model.getProperty(namespace + "hasTitle"), title);
        blogResource.addProperty(model.getProperty(namespace + "hasContent"), content);
        blogResource.addProperty(model.getProperty(namespace + "hasDate"), date);
        blogResource.addProperty(model.getProperty(namespace + "hasType"), type.name());

        saveRDF();
    }

    public void updateBlog(String id, String title, String content, String date, BlogType type) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource blogResource = model.getResource(namespace + id);

        if (blogResource != null) {
            blogResource.removeAll(model.getProperty(namespace + "hasTitle"));
            blogResource.addProperty(model.getProperty(namespace + "hasTitle"), title);

            blogResource.removeAll(model.getProperty(namespace + "hasContent"));
            blogResource.addProperty(model.getProperty(namespace + "hasContent"), content);

            blogResource.removeAll(model.getProperty(namespace + "hasDate"));
            blogResource.addProperty(model.getProperty(namespace + "hasDate"), date);

            blogResource.removeAll(model.getProperty(namespace + "hasType"));
            blogResource.addProperty(model.getProperty(namespace + "hasType"), type.name());

            saveRDF();
        }
    }

    public String getBlogs() {
        loadRDF();

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        String queryString = "PREFIX ontology: <" + namespace + "> "
                + "SELECT ?Blog ?hasTitle ?hasDate ?hasContent ?hasType "
                + "WHERE { "
                + "  ?Blog a ontology:Blog . "
                + "  ?Blog ontology:hasTitle ?hasTitle . "
                + "  ?Blog ontology:hasDate ?hasDate . "
                + "  ?Blog ontology:hasContent ?hasContent . "
                + "  ?Blog ontology:hasType ?hasType . "
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
                blogObject.put("title", solution.getLiteral("hasTitle").getString());
                blogObject.put("date", solution.getLiteral("hasDate").getString());
                blogObject.put("content", solution.getLiteral("hasContent").getString());
                blogObject.put("type", solution.getLiteral("hasType").getString());

                blogsArray.put(blogObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("blogs", blogsArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying blogs: " + e.getMessage());
        }
    }

    public void deleteBlog(String id) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource blogResource = model.getResource(namespace + id);

        if (blogResource != null) {
            model.removeAll(blogResource, null, null);
            saveRDF();
        }
    }
    public String getBlogById(String id) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource blogResource = model.getResource(namespace + id);

        if (blogResource != null && blogResource.hasProperty(RDF.type, model.getResource(namespace + "Blog"))) {
            JSONObject blogObject = new JSONObject();
            blogObject.put("id", id);

            if (blogResource.hasProperty(model.getProperty(namespace + "hasTitle"))) {
                blogObject.put("title", blogResource.getProperty(model.getProperty(namespace + "hasTitle")).getString());
            }
            if (blogResource.hasProperty(model.getProperty(namespace + "hasDate"))) {
                blogObject.put("date", blogResource.getProperty(model.getProperty(namespace + "hasDate")).getString());
            }
            if (blogResource.hasProperty(model.getProperty(namespace + "hasContent"))) {
                blogObject.put("content", blogResource.getProperty(model.getProperty(namespace + "hasContent")).getString());
            }
            if (blogResource.hasProperty(model.getProperty(namespace + "hasType"))) {
                blogObject.put("type", blogResource.getProperty(model.getProperty(namespace + "hasType")).getString());
            }

            return blogObject.toString();
        } else {
            throw new IllegalArgumentException("Blog with ID " + id + " not found.");
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
