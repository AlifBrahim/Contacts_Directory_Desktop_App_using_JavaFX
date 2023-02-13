package com.example.contacts;

import com.example.contacts.demoCommunicationBtwinControllers.Controller1;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditContactController {
    @FXML
    private TextField editNameField;
    @FXML private TextField editPhoneField;
    @FXML private TextField editEmailField;
    @FXML private TextField editAddressField;
    @FXML private DatePicker editBirthdayField;
    @FXML private TextArea editNoteField;
    @FXML
    private Button doneEditButton;

    @FXML private Button cancelEditButton;

    @FXML private Button uploadImageButton;
    @FXML private Button removeImageButton;
    @FXML private ImageView imageView;
    private final Stage thisStage;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ContactsDirectoryController contactsDirectoryController;
    public EditContactController(ContactsDirectoryController contactsDirectoryController) {
        // We received the first controller, now let's make it usable throughout this controller.
        this.contactsDirectoryController = contactsDirectoryController; ;

        // Create the new stage
        thisStage = new Stage();

        // Load the FXML file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditContact.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            thisStage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            thisStage.setTitle("Edit Contact");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        setTextFields();
        doneEditButton.setOnAction(e -> doneEditButtonClicked());
        cancelEditButton.setOnAction(e -> thisStage.close());
        uploadImageButton.setOnAction(e -> uploadImage());
        removeImageButton.setOnAction(e -> removeImage());
        if (imageView.getImage() == null) {
            removeImageButton.setDisable(true);
            removeImageButton.setVisible(false);
        }

    }

    /**
     * Show the stage that was loaded in the constructor
     */
    public void showStage() {
        thisStage.showAndWait();
    }

    public void setTextFields() {
        // set text of editNameField to the nameLabel from contactsDirectoryController
        editNameField.setText(contactsDirectoryController.getNameLabel().getText());
        editPhoneField.setText(contactsDirectoryController.getPhoneLabel().getText());
        editEmailField.setText(contactsDirectoryController.getEmailLabel().getText());
        editAddressField.setText(contactsDirectoryController.getAddressLabel().getText());
        // edit birthday field to the birthday label from contactsDirectoryController with the format of DatePicker
        // if the birthday label is empty, set the value of editBirthdayField to null
        if (contactsDirectoryController.getBirthdayLabel().getText().equals("")) {
            editBirthdayField.setValue(null);
        } else {
            editBirthdayField.setValue(LocalDate.parse(contactsDirectoryController.getBirthdayLabel().getText(), formatter));
        }
        editNoteField.setText(contactsDirectoryController.getNotesLabel().getText());
        imageView.setImage(contactsDirectoryController.getImageView().getImage());
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

    public void removeImage() {
        imageView.setImage(null);
    }
    public void doneEditButtonClicked() {
        contactsDirectoryController.getNameLabel().setText(editNameField.getText());
        contactsDirectoryController.getPhoneLabel().setText(editPhoneField.getText());
        contactsDirectoryController.getEmailLabel().setText(editEmailField.getText());
        contactsDirectoryController.getAddressLabel().setText(editAddressField.getText());
        // set birthday label to the birthday field with the format of DatePicker
        contactsDirectoryController.getBirthdayLabel().setText(String.valueOf(editBirthdayField.getValue()));
        contactsDirectoryController.getNotesLabel().setText(editNoteField.getText());
        contactsDirectoryController.getImageView().setImage(imageView.getImage());


        // update the contact in the list

        thisStage.close();
    }



}
