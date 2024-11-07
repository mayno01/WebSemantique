package com.example.websemantique.Controlleur;


public class ReclamationResponseRequest {
    private String reclamationId;
    private String responseId;
    private String responseText;
    private String date;
    private String type;

    // Getters and setters
    public String getReclamationId() {
        return reclamationId;
    }

    public void setReclamationId(String reclamationId) {
        this.reclamationId = reclamationId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
