package com.app.roktoDorkar.api.upazilaApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpzilaModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("upazila")
    @Expose
    private ArrayList<Upazila> upazila = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Upazila> getUpazila() {
        return upazila;
    }

    public void setUpazila(ArrayList<Upazila> upazila) {
        this.upazila = upazila;
    }
    public class Upazila {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("upazila")
        @Expose
        private String upazila;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUpazila() {
            return upazila;
        }

        public void setUpazila(String upazila) {
            this.upazila = upazila;
        }

    }
}
