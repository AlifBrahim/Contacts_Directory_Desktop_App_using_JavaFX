package com.example.contacts;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

import java.util.Date;

public class Contact extends Favourite implements Comparable<Contact>{
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty phone = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private Date dateAdded;
    private String imagePath;



    public Contact(String name, String phone, String email) {
        super(name, phone, email, "");
        this.setName(name);
        this.setPhone(phone);
        this.setEmail(email);
        this.dateAdded = new Date();
    }
    public Contact(String name, String phone, String email, String imagePath) {
        super(name, phone, email, imagePath);
        this.setName(name);
        this.setPhone(phone);
        this.setEmail(email);
        this.setImagePath(imagePath);
        this.dateAdded = new Date();
    }

    public Contact() {
        this("", "", "");
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public Date getDateAdded() {
        return dateAdded;
    }


    @Override
    public String toString() {
        return "Favourite{" +
                "name=" + name.get() +
                ", phone=" + phone.get() +
                ", email=" + email.get() +
                ", imagePath='" + getImagePath() + '\'' +
                ", dateAdded=" + getDateAdded() +
                '}';
    }

    @Override
    public int compareTo(Contact o) {
        return 0;
    }
}
