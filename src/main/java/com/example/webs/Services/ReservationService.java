package com.example.webs.Services;

import com.example.webs.Enums.ReservationType;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.InputStream;

@Component
public class ReservationService {

    private static final String RDF_FILE_PATH = "C:/Users/MOLKA/Downloads/webSem.rdf";
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

    public void addReservation(String id, String type, String date, String description, String feedback) {
        if (model == null) {
            loadRDF();
        }

        ReservationType reservationType;
        try {
            reservationType = ReservationType.valueOf(type.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ReservationType provided");
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource reservationResource = model.createResource(namespace + id);
        reservationResource.addProperty(RDF.type, model.getResource(namespace + "Reservation"));
        reservationResource.addProperty(model.getProperty(namespace + "Reservation_type"), reservationType.toString());
        reservationResource.addProperty(model.getProperty(namespace + "Reservation_date"), date);
        reservationResource.addProperty(model.getProperty(namespace + "Reservation_description"), description);
        reservationResource.addProperty(model.getProperty(namespace + "Reservation_feedback"), feedback);

        saveRDF();
    }

    public void updateReservation(String id, String type, String date, String description, String feedback) {
        if (model == null) {
            loadRDF();
        }

        ReservationType reservationType;
        try {
            reservationType = ReservationType.valueOf(type.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ReservationType provided");
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource reservationResource = model.getResource(namespace + id);

        if (reservationResource != null) {
            reservationResource.removeAll(model.getProperty(namespace + "Reservation_type"));
            reservationResource.addProperty(model.getProperty(namespace + "Reservation_type"), reservationType.toString());

            reservationResource.removeAll(model.getProperty(namespace + "Reservation_date"));
            reservationResource.addProperty(model.getProperty(namespace + "Reservation_date"), date);

            reservationResource.removeAll(model.getProperty(namespace + "Reservation_description"));
            reservationResource.addProperty(model.getProperty(namespace + "Reservation_description"), description);

            reservationResource.removeAll(model.getProperty(namespace + "Reservation_feedback"));
            reservationResource.addProperty(model.getProperty(namespace + "Reservation_feedback"), feedback);

            saveRDF();
        }
    }

    public void deleteReservation(String id) {
        if (model == null) {
            loadRDF();
        }

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        Resource reservationResource = model.getResource(namespace + id);

        if (reservationResource != null) {
            model.removeAll(reservationResource, null, null);
            model.removeAll(null, null, reservationResource);

            saveRDF();
        }
    }

    public String getReservations() {
        loadRDF();

        String namespace = "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#";
        String queryString = "PREFIX ontology: <" + namespace + "> "
                + "SELECT ?Reservation ?Reservation_type ?Reservation_date ?Reservation_description ?Reservation_feedback "
                + "WHERE { "
                + "  ?Reservation a ontology:Reservation . "
                + "  ?Reservation ontology:Reservation_type ?Reservation_type . "
                + "  ?Reservation ontology:Reservation_date ?Reservation_date . "
                + "  ?Reservation ontology:Reservation_description ?Reservation_description . "
                + "  ?Reservation ontology:Reservation_feedback ?Reservation_feedback . "
                + "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            StringBuilder resultJson = new StringBuilder("[");
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String idURL = soln.getResource("Reservation").toString();
                String id = idURL.split("#")[1];
                String type = soln.getLiteral("Reservation_type").getString();
                String date = soln.getLiteral("Reservation_date").getString();
                String description = soln.getLiteral("Reservation_description").getString();
                String feedback = soln.getLiteral("Reservation_feedback").getString();
                resultJson.append("{\"id\":\"").append(id)
                        .append("\", \"type\":\"").append(type)
                        .append("\", \"date\":\"").append(date)
                        .append("\", \"description\":\"").append(description)
                        .append("\", \"feedback\":\"").append(feedback)
                        .append("\"},");
            }
            resultJson.deleteCharAt(resultJson.length() - 1).append("]");
            return resultJson.toString();
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
