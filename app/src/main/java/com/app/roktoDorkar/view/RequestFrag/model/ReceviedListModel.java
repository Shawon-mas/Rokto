package com.app.roktoDorkar.view.RequestFrag.model;

import java.io.Serializable;

public class ReceviedListModel implements Serializable {
    private String bloodGroup;
    private String userName;
    private String userEmail;
    private String location;
    private String requestType;
    private String senderUid;
    private String receiverUid;

    public ReceviedListModel()
    {
    }

    public ReceviedListModel(String bloodGroup, String userName, String userEmail, String location, String requestType, String senderUid, String receiverUid) {
        this.bloodGroup = bloodGroup;
        this.userName = userName;
        this.userEmail = userEmail;
        this.location = location;
        this.requestType = requestType;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }
}
