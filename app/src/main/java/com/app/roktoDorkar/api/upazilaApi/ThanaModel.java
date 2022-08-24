package com.app.roktoDorkar.api.upazilaApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ThanaModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("up_thana")
    @Expose
    private ArrayList<UpThana> upThana = null;
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

    public ArrayList<UpThana> getUpThana() {
        return upThana;
    }

    public void setUpThana(ArrayList<UpThana> upThana) {
        this.upThana = upThana;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }
    public class UpThana {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
