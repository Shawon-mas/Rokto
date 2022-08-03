package com.app.roktoDorkar.api.upazilaApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DisDivModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("upazila")
    @Expose
    private ArrayList<DisDiv> disdiv = null;
    @SerializedName("load_time")
    @Expose
    private String loadTime;

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

    public ArrayList<DisDiv> getDisDiv() {
        return disdiv;
    }

    public void setUpazila(ArrayList<DisDiv> disdiv) {
        this.disdiv = disdiv;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }
    public class DisDiv {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("division")
        @Expose
        private String division;
        @SerializedName("district")
        @Expose
        private String district;
        @SerializedName("upazila")
        @Expose
        private String upazila;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDivision() {
            return division;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getUpazila() {
            return upazila;
        }

        public void setUpazila(String upazila) {
            this.upazila = upazila;
        }

    }
}
