package com.example.contacts;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.util.Date;

public class Contact implements Comparable<Contact>{
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty phone = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private String address;
    private String birthday;
    private String notes;
    private Date dateAdded;
    private String imagePath;



    public Contact(String name, String phone, String email, String address, String birthday, String notes) {
        this.setName(name);
        this.setPhone(phone);
        this.setEmail(email);
        this.setAddress(address);
        this.setBirthday(birthday);
        this.setNotes(notes);
        this.dateAdded = new Date();
    }
    public Contact(String name, String phone, String email, String address, String birthday, String notes, String imagePath) {
        this.setName(name);
        this.setPhone(phone);
        this.setEmail(email);
        this.setAddress(address);
        this.setBirthday(birthday);
        this.setNotes(notes);
        this.setImagePath(imagePath);
        this.dateAdded = new Date();
    }

    public Contact() {
        this("", "", "", "", "", "");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        return "Contact{" +
                "name=" + name.get() +
                ", phone=" + phone.get() +
                ", email=" + email.get() +
                ", address='" + address + '\'' +
                ", birthday='" + birthday + '\'' +
                ", notes='" + notes + '\'' +
                ", dateAdded=" + dateAdded +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    @Override
    public int compareTo(Contact o) {
        return 0;
    }

    public String toCSV() {
        return "Name,Phone Number,Email,Address,Birthday,Notes,Image Path" + System.lineSeparator()
                + name.get() + "," + phone.get() + "," + email.get() + "," + address + "," + birthday + "," + notes + "," + imagePath;
    }

    // public String toCSV() with column names
}
