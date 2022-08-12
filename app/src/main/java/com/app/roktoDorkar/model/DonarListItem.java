package com.app.roktoDorkar.model;

import java.io.Serializable;

public class DonarListItem implements Serializable {
    private String name;
    private String bloodType;
    private String upazila;
    private String age;
    private String email;
    private String uId;
    private String imageUri;

    public DonarListItem() {
    }

    public DonarListItem(String name, String bloodType, String upazila, String age, String email, String uId, String imageUri) {
        this.name = name;
        this.bloodType = bloodType;
        this.upazila = upazila;
        this.age = age;
        this.email = email;
        this.uId = uId;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getUpazila() {
        return upazila;
    }

    public void setUpazila(String upazila) {
        this.upazila = upazila;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
