package com.example.contacts;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CreateContactController {
    @FXML private ImageView imageView;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private DatePicker birthdayDatePicker;
    @FXML private TextArea notesArea;
    @FXML private Button uploadImageButton;

    private final Stage thisStage;
    private final ContactsDirectoryController contactsDirectoryController;

    public CreateContactController (ContactsDirectoryController contactsDirectoryController) {
        // We received the first controller, now let's make it usable throughout this controller.
        this.contactsDirectoryController = contactsDirectoryController;

        // Create the new stage
        thisStage = new Stage();

        // Load the FXML file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateContact.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            thisStage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            thisStage.setTitle("Create Contact");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showStage() {
        thisStage.showAndWait();
    }
    @FXML
    public void initialize() {
        saveButton.setOnAction(event -> saveButtonClicked());
        cancelButton.setOnAction(event -> thisStage.close());
        uploadImageButton.setOnAction(event -> uploadImage());

        // Add a listener to the birthday date picker
        birthdayDatePicker.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidDate(newValue)) {
                System.out.println("Invalid date. Please enter a valid date.");
            }
        });
    }

    public void saveButtonClicked() {
        String birthday = null;
        String image = null;
        if (nameField.getText().isEmpty() && phoneField.getText().isEmpty()) {
            return;
        }
        // Get the data from the fields
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        if (birthdayDatePicker.getValue() != null) {
            birthday = birthdayDatePicker.getValue().toString();
        }
        String notes = notesArea.getText();
        if (imageView.getImage() != null){
            image = imageView.getImage().getUrl();
        }

        // Create a new contact
        Contact newContact = new Contact(name, phone, email, address, birthday, notes, image);

        // Add the contact to the list
        contactsDirectoryController.getContacts().add(newContact);
        // if birthday.

        // Update the table
        contactsDirectoryController.getTableView().setItems(FXCollections.observableArrayList(contactsDirectoryController.getContacts()));
        ////        tableView.getItems().add(new Favourite(nameField.getText(), phoneField.getText(), emailField.getText()));

        System.out.println(contactsDirectoryController.getContacts());
        System.out.println(contactsDirectoryController.getTableView().getItems());
        // Close the window
        thisStage.close();
    }

    private boolean isValidDate(String date) {
        // Date format to check the input against
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);

        try {
            // Parsing the date string and checking if it's a valid date
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    public void uploadImage() {
        Contact newContact = new Contact();
        Image contactImage;
        Stage stage = new Stage();
        //create a file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            newContact.setImagePath(selectedFile.getAbsolutePath());
            contactImage = new Image(newContact.getImagePath());
            imageView.setImage(contactImage);
        }
    }


    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public TextField getNameField() {
        return nameField;
    }

    public void setNameField(TextField nameField) {
        this.nameField = nameField;
    }

    public TextField getPhoneField() {
        return phoneField;
    }

    public void setPhoneField(TextField phoneField) {
        this.phoneField = phoneField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public void setEmailField(TextField emailField) {
        this.emailField = emailField;
    }

    public TextField getAddressField() {
        return addressField;
    }

    public void setAddressField(TextField addressField) {
        this.addressField = addressField;
    }

    public DatePicker getBirthdayDatePicker() {
        return birthdayDatePicker;
    }

    public void setBirthdayDatePicker(DatePicker birthdayDatePicker) {
        this.birthdayDatePicker = birthdayDatePicker;
    }

    public TextArea getNotesArea() {
        return notesArea;
    }

    public void setNotesArea(TextArea notesArea) {
        this.notesArea = notesArea;
    }

    public Stage getThisStage() {
        return thisStage;
    }

    public ContactsDirectoryController getContactsTestController() {
        return contactsDirectoryController;
    }
}
