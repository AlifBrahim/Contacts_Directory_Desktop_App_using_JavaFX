package com.example.contacts;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ContactsApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ContactsDirectoryController contactsDirectoryController = new ContactsDirectoryController();

        contactsDirectoryController.showStage();
    }

    public static void main(String[] args) {
        launch();
    }
}