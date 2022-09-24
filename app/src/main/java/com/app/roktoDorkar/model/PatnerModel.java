package com.app.roktoDorkar.model;

import java.io.Serializable;

public class PatnerModel implements Serializable {
    String imageUri;
    String name;
    String quantity;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    String link;

    public PatnerModel() {
    }

    public PatnerModel(String imageUri, String name, String quantity, String link) {
        this.imageUri = imageUri;
        this.name = name;
        this.quantity = quantity;
        this.link = link;
    }


    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
