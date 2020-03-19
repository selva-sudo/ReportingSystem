package com.cadmin.myadmin.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class ComplaintModel implements Serializable {
    public String complaint;
    public String area;
    public String complaintId;
    public String imageUrl;
    public String status;
    public String officerId;


    public ComplaintModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ComplaintModel(String complaint, String area,String complaintId,String imageUrl,String status,String officerId) {
        this.complaint = complaint;
        this.area = area;
        this.complaintId = complaintId;
        this.imageUrl = imageUrl;
        this.status = status;
        this.officerId = officerId;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOfficerId() {
        return officerId;
    }

    public void setOfficerId(String officerId) {
        this.officerId = officerId;
    }
}
