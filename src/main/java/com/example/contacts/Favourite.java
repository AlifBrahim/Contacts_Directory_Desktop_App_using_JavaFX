package com.example.contacts;

import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class Favourite {
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty phone = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private Date dateAdded;
    private String imagePath;



    public Favourite(String name, String phone, String email) {
        this.setNameFav(name);
        this.setPhoneFav(phone);
        this.setEmailFav(email);
        this.dateAdded = new Date();
    }
    public Favourite(String name, String phone, String email, String imagePath) {
        this.setNameFav(name);
        this.setPhoneFav(phone);
        this.setEmailFav(email);
        this.setImagePathFav(imagePath);
        this.dateAdded = new Date();
    }

    public Favourite() {
        this("", "", "");
    }
    public String getNameFav() {
        return name.get();
    }

    public SimpleStringProperty namePropertyFav() {
        return name;
    }

    public void setNameFav(String name) {
        this.name.set(name);
    }

    public String getPhoneFav() {
        return phone.get();
    }

    public SimpleStringProperty phonePropertyFav() {
        return phone;
    }

    public void setPhoneFav(String phone) {
        this.phone.set(phone);
    }

    public String getEmailFav() {
        return email.get();
    }

    public SimpleStringProperty emailPropertyFav() {
        return email;
    }

    public void setEmailFav(String email) {
        this.email.set(email);
    }
    public String getImagePathFav() {
        return imagePath;
    }
    public void setImagePathFav(String imagePath) {
        this.imagePath = imagePath;
    }
    public Date getDateAddedFav() {
        return dateAdded;
    }


    @Override
    public String toString() {
        return "Favourite{" +
                "name=" + name.get() +
                ", phone=" + phone.get() +
                ", email=" + email.get() +
                ", imagePath='" + getImagePathFav() + '\'' +
                ", dateAdded=" + getDateAddedFav() +
                '}';
    }



}
