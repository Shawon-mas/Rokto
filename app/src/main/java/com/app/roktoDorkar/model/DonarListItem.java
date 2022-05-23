package com.app.roktoDorkar.model;

import java.io.Serializable;

public class DonarListItem implements Serializable {
    private String userName;
    private String bloodType;
    private String upzilla;
    private String userAge;
    private String userEmail;
    private String uId;

    public DonarListItem() {

    }

    public DonarListItem(String userName, String bloodType, String upzilla, String userAge, String userEmail, String uId) {
        this.userName = userName;
        this.bloodType = bloodType;
        this.upzilla = upzilla;
        this.userAge = userAge;
        this.userEmail = userEmail;
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getUpzilla() {
        return upzilla;
    }

    public void setUpzilla(String upzilla) {
        this.upzilla = upzilla;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
