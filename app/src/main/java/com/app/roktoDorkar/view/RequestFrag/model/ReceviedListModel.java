package com.app.roktoDorkar.view.RequestFrag.model;

import java.io.Serializable;

public class ReceviedListModel implements Serializable {
    private String requestStatus;
    private String senderGiftAmount;
    private String senderName;
    private String senderPatientGender;
    private String senderPhoneNumber;
    private String senderRequestDetails;
    private String senderRequestForDate;
    private String senderRequestForTime;
    private String senderRequestLocation;
    private String senderRequestUpazila;
    private String senderRequiredBlood;
    private String senderRequiredQuantity;
    private String senderUid;
    private String senderEmail;
    private String requestReceiverUid;
    private String senderId;

    public ReceviedListModel() {
    }

    public ReceviedListModel(String requestStatus, String senderGiftAmount, String senderName, String senderPatientGender, String senderPhoneNumber, String senderRequestDetails, String senderRequestForDate, String senderRequestForTime, String senderRequestLocation, String senderRequestUpazila, String senderRequiredBlood, String senderRequiredQuantity, String senderUid, String senderEmail, String requestReceiverUid, String senderId) {
        this.requestStatus = requestStatus;
        this.senderGiftAmount = senderGiftAmount;
        this.senderName = senderName;
        this.senderPatientGender = senderPatientGender;
        this.senderPhoneNumber = senderPhoneNumber;
        this.senderRequestDetails = senderRequestDetails;
        this.senderRequestForDate = senderRequestForDate;
        this.senderRequestForTime = senderRequestForTime;
        this.senderRequestLocation = senderRequestLocation;
        this.senderRequestUpazila = senderRequestUpazila;
        this.senderRequiredBlood = senderRequiredBlood;
        this.senderRequiredQuantity = senderRequiredQuantity;
        this.senderUid = senderUid;
        this.senderEmail = senderEmail;
        this.requestReceiverUid = requestReceiverUid;
        this.senderId = senderId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getSenderGiftAmount() {
        return senderGiftAmount;
    }

    public void setSenderGiftAmount(String senderGiftAmount) {
        this.senderGiftAmount = senderGiftAmount;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPatientGender() {
        return senderPatientGender;
    }

    public void setSenderPatientGender(String senderPatientGender) {
        this.senderPatientGender = senderPatientGender;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public String getSenderRequestDetails() {
        return senderRequestDetails;
    }

    public void setSenderRequestDetails(String senderRequestDetails) {
        this.senderRequestDetails = senderRequestDetails;
    }

    public String getSenderRequestForDate() {
        return senderRequestForDate;
    }

    public void setSenderRequestForDate(String senderRequestForDate) {
        this.senderRequestForDate = senderRequestForDate;
    }

    public String getSenderRequestForTime() {
        return senderRequestForTime;
    }

    public void setSenderRequestForTime(String senderRequestForTime) {
        this.senderRequestForTime = senderRequestForTime;
    }

    public String getSenderRequestLocation() {
        return senderRequestLocation;
    }

    public void setSenderRequestLocation(String senderRequestLocation) {
        this.senderRequestLocation = senderRequestLocation;
    }

    public String getSenderRequestUpazila() {
        return senderRequestUpazila;
    }

    public void setSenderRequestUpazila(String senderRequestUpazila) {
        this.senderRequestUpazila = senderRequestUpazila;
    }

    public String getSenderRequiredBlood() {
        return senderRequiredBlood;
    }

    public void setSenderRequiredBlood(String senderRequiredBlood) {
        this.senderRequiredBlood = senderRequiredBlood;
    }

    public String getSenderRequiredQuantity() {
        return senderRequiredQuantity;
    }

    public void setSenderRequiredQuantity(String senderRequiredQuantity) {
        this.senderRequiredQuantity = senderRequiredQuantity;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRequestReceiverUid() {
        return requestReceiverUid;
    }

    public void setRequestReceiverUid(String requestReceiverUid) {
        this.requestReceiverUid = requestReceiverUid;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
