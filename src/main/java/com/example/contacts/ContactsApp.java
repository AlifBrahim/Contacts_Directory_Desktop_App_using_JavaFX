package com.example.contacts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ContactsApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ContactsApp.class.getResource("ContactsApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        // maximize the window
        stage.setMaximized(true);
        stage.setTitle("Contacts App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}