package com.example.webs.Controllers;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {
    private String reclamationId;
    private String responseText;
    private String date;
    private String type;  // This is a String field that will hold the type value from the request

    // Getters and setters
    public String getReclamationId() {
        return reclamationId;
    }

    public void setReclamationId(String reclamationId) {
        this.reclamationId = reclamationId;
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

