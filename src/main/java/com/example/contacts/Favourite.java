package com.example.contacts;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.util.Date;

public class Favourite extends Contact {

    public Favourite(String name, String phone, String email, String address, String birthday, String notes) {
        super(name, phone, email, address, birthday, notes);
    }
    public Favourite(String name, String phone, String email, String address, String birthday, String notes, String imagePath) {
        super(name, phone, email, address, birthday, notes, imagePath);
    }

    public Favourite() {
        this("", "", "", "", "", "");
    }


    // toString method
    @Override
    public String toString() {
        return "Favourite{" +
                "name=" + getName() +
                ", phone=" + getPhone() +
                ", email=" + getEmail() +
                ", address=" + getAddress() +
                ", birthday=" + getBirthday() +
                ", notes=" + getNotes() +
                ", dateAdded=" + getDateAdded() +
                ", imagePath=" + getImagePath() +
                '}';

    }


}
