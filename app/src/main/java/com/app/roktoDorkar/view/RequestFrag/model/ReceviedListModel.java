package com.app.roktoDorkar.view.RequestFrag.model;

import java.io.Serializable;

public class ReceviedListModel implements Serializable {
    private String bloodGroup;
    private String userName;
    private String location;
    private Boolean requestType;

    public ReceviedListModel() {
    }

    public ReceviedListModel(String bloodGroup, String userName, String location, Boolean requestType) {
        this.bloodGroup = bloodGroup;
        this.userName = userName;
        this.location = location;
        this.requestType = requestType;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getRequestType() {
        return requestType;
    }

    public void setRequestType(Boolean requestType) {
        this.requestType = requestType;
    }
}
