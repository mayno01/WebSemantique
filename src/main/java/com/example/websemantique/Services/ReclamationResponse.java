package com.example.websemantique.Services;

import com.example.websemantique.Enums.TypeReponse;

public class ReclamationResponse {

    private String responseText;
    private String date;
    private TypeReponse type;

    // Getters and setters
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

    public TypeReponse getType() {
        return type;
    }

    public void setType(TypeReponse type) {
        this.type = type;
    }
}
