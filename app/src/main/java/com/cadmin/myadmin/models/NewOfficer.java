package com.cadmin.myadmin.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class NewOfficer {
    public String id;
    public String name;
    public String phone;
    public String email;
    public String area;
    public String address;


    public NewOfficer(){

    }
    public NewOfficer(String id,String name,String phone,String email,String area,String address){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.area = area;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
