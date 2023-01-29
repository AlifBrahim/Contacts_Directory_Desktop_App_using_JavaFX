package com.example.contacts;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.Predicate;

public class ContactsAppController {

    LinkedList<Contact> contacts = new LinkedList<>();
    LinkedList<Favourite> favourites = new LinkedList<>();
    @FXML
    private TableView<Contact> tableView;
    @FXML private TableView<Favourite> favouriteTableView;

    @FXML
    private TableColumn<Contact, String> nameCol;
    @FXML
    private TableColumn<Contact, String> phoneCol;
    @FXML
    private TableColumn<Contact, String> emailCol;

    @FXML
    private TableColumn<Favourite, String> nameFavCol;
    @FXML
    private TableColumn<Favourite, String> phoneFavCol;
    @FXML
    private TableColumn<Favourite, String> emailFavCol;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField searchContactsField;
    @FXML private Button createContactButton;

    @FXML private Button editContactButton;
    @FXML
    private Button doneEditButton;

    @FXML private Button cancelEditButton;

    @FXML private ComboBox<String> sortByComboBox;

    @FXML private Button uploadContactImageButton;
    @FXML private Button uploadCreateContactImageButton;

    @FXML private ImageView createContactImageView;
    @FXML private Button goBackButton;
    @FXML private MenuItem addToFavouritesContextMenu;
    @FXML private TableColumn<Contact, String> favCol;
    @FXML private ImageView contactImageView;
    @FXML private  Label nameLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;
    @FXML private Label noteLabel;


    Sorter sorter = new Sorter();
    Stage stage = new Stage();
    private Contact selectedContact = new Contact();
    private Image contactImage;
    ObservableList<Favourite> observableFavourites = FXCollections.observableList(favourites);


    public void initialize() {
        // set up the columns in the table
        nameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("phoneField"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("emailField"));
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        // set up the columns in the favourite table
        nameFavCol.setCellValueFactory(new PropertyValueFactory<Favourite, String>("name"));
        phoneFavCol.setCellValueFactory(new PropertyValueFactory<Favourite, String>("phone"));
        emailFavCol.setCellValueFactory(new PropertyValueFactory<Favourite, String>("email"));
        nameFavCol.setCellValueFactory(cellData -> cellData.getValue().namePropertyFav());
        phoneFavCol.setCellValueFactory(cellData -> cellData.getValue().phonePropertyFav());
        emailFavCol.setCellValueFactory(cellData -> cellData.getValue().emailPropertyFav());

        sortByComboBox.getItems().addAll("Name", "Phone", "Email", "Recently Added"); // add items to the combo box
        doneEditButton.setDisable(true); // disable the done edit button
        cancelEditButton.setDisable(true); // disable the cancel edit button

        // ----------------- change image for contact -----------------
        uploadContactImageButton.setOnAction(new EventHandler<ActionEvent>() { // when user clicks on the upload contact image button
            @Override
            public void handle(ActionEvent event) {
                if (tableView.getSelectionModel().getSelectedItem() != null) {
                    selectedContact = tableView.getSelectionModel().getSelectedItem(); // update the selected contact
                    //create a file chooser
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select an image file");
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
                    File selectedFile = fileChooser.showOpenDialog(stage);
                    if (selectedFile != null) {
                        selectedContact.setImagePath(selectedFile.getAbsolutePath());
                        contactImage = new Image(selectedContact.getImagePath());
                        // when user clicks on other row, the image will be cleared or updated if the next row has imagePath
                        // and when user clicks on the createContactButton, the image will be cleared
                        if (selectedContact.getImagePath() != null ) {
                            contactImageView.setImage(contactImage);
                        } else {
                            contactImageView.setImage(null);
                        }
                    }
                }
            }
        });




        tableView.setRowFactory(tv -> { // when user clicks on a row, the image will be displayed
            TableRow<Contact> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    Contact rowData = row.getItem();
                    System.out.println("Clicked on: " + rowData.getName());
                    System.out.println("Image Path: " + rowData.getImagePath());
                    System.out.println();
                    // when user clicks on other row, the image will be cleared or updated if the next row has imagePath
                    if (rowData.getImagePath() != null) {
                        contactImage = new Image(rowData.getImagePath());
                        contactImageView.setImage(contactImage);
                        nameLabel.setText(rowData.getName());
                        phoneLabel.setText(rowData.getPhone());
                        emailLabel.setText(rowData.getEmail());
                    }
                    else if (rowData.getName() != null || rowData.getPhone() != null || rowData.getEmail() != null){
                        contactImageView.setImage(null);
                        nameLabel.setText(rowData.getName());
                        phoneLabel.setText(rowData.getPhone());
                        emailLabel.setText(rowData.getEmail());
                    }
                    else {
                        contactImageView.setImage(null);
                        nameLabel.setText(null);
                        phoneLabel.setText(null);
                        emailLabel.setText(null);
                    }
                }
            });
            return row;
        });
        nameField.setOnMouseClicked(new EventHandler<MouseEvent>() { // when user clicks on the nameField
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
//                    contactImageView.setImage(null);
                }
            }
        });
        phoneField.setOnMouseClicked(new EventHandler<MouseEvent>() { // when user clicks on the phoneField
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    contactImageView.setImage(null);
                }
            }
        });

        emailField.setOnMouseClicked(new EventHandler<MouseEvent>() { // when user clicks on the emailField
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    contactImageView.setImage(null);
                }
            }
        });

        // ----------------- Upload image when creating contact -----------------
        uploadCreateContactImageButton.setOnAction(new EventHandler<ActionEvent>() { // when user clicks on the uploadCreateContactImageButton
            @Override
            public void handle(ActionEvent event) {
                // if the text fields are empty, don't proceed
                if (nameField.getText().isEmpty() && phoneField.getText().isEmpty() && emailField.getText().isEmpty()) {
                    return;
                }
                Contact newContact = new Contact();
                //create a file chooser
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select an image file");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    newContact.setImagePath(selectedFile.getAbsolutePath());
                    contactImage = new Image(newContact.getImagePath());
                    createContactImageView.setImage(contactImage);
                    // when createContactButton is clicked, set the image path to the new contact
                    createContactButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //create new contact with input information
                            Contact newContact = new Contact(nameField.getText(), phoneField.getText(), emailField.getText());
                            //set image path of new contact to selected file path
                            newContact.setImagePath(selectedFile.getAbsolutePath());
                            //add new contact to contacts list
                            contacts.add(newContact);
                            //update table view
                            tableView.setItems(FXCollections.observableList(contacts));
                            createContactImageView.setImage(null);
                            clearTextFields();
                        }
                    });

                }


            }
        });


        favCol.setCellFactory(column -> {
            return new TableCell<Contact, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Contact contact = (Contact) getTableRow().getItem();
                        if (favourites.contains(contact)) {
                            System.out.println("slash added");
                            setText("/");
                        } else {
                            setText(null);
                        }
                    }
                }
            };
        });






    }


    @FXML
    private void handleTableClick(MouseEvent event) throws IOException {
        // ... existing code ...

    }
    public void CreateContact (ActionEvent event) throws IOException {
        // don't proceed if the text fields are empty
        if (nameField.getText().isEmpty() && phoneField.getText().isEmpty() && emailField.getText().isEmpty()) {
            clearTextFields();
            return;
        }
        // create a new contact
        Contact newContact = new Contact(nameField.getText(), phoneField.getText(), emailField.getText());

        // create a new contact if the uploadContactImageButton is clicked
        if (uploadCreateContactImageButton.isPressed()) {
            newContact = new Contact(nameField.getText(), phoneField.getText(), emailField.getText(), selectedContact.getImagePath());
        }

        // add contact to linked list
        contacts.add(newContact);
        // make linked list compatible with table
        tableView.setItems(FXCollections.observableArrayList(contacts));
        // clear text fields
        clearTextFields();
//        // add contact to table
//        tableView.getItems().add(new Favourite(nameField.getText(), phoneField.getText(), emailField.getText()));
    }
    public void deleteContact(ActionEvent event) throws IOException {
        // get the selected contact
        Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
        // remove contact from table
        tableView.getItems().remove(selectedContact);
        // remove contact from list
        contacts.remove(selectedContact);

        if (favouriteTableView.isVisible()){
            Favourite selectedFavourite = favouriteTableView.getSelectionModel().getSelectedItem();
            favouriteTableView.getItems().remove(selectedContact);
            favourites.remove(selectedFavourite);
        }
    }



    // --------------------------------- Upload CSV ---------------------------------
    @FXML
    private void uploadCSV() {
        FileChooser fileChooser = new FileChooser(); // create a file chooser
        fileChooser.setInitialDirectory(new File("C:\\Users\\DELL\\Downloads")); // set the initial directory
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().addAll( // add filters to the file chooser
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null); // open the file chooser
        if (selectedFile != null) { // if a file is selected
            try {
                // read CSV file and add data to the contacts linked list
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] contact = line.split(",");
                    String name = contact[0];
                    String phone = contact[1];
                    String email = contact[2];
                    // add data to the contacts linked list
                    contacts.add(new Contact(name, phone, email));
                }
                // populate the tableview with the contacts linked list
                tableView.setItems(FXCollections.observableArrayList(contacts));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void sortBy (ActionEvent event) throws IOException{
        String selected = sortByComboBox.getValue(); // get the selected item from the combo box
        switch (selected) { // sort the contacts linked list based on the selected item
            case "Name" -> {
                sorter.quickSort(contacts, Comparator.comparing(Contact::getName, String.CASE_INSENSITIVE_ORDER));
                sorter.printSorted(contacts);
            }
            case "Phone" -> {
                Sorter.insertionSortPhoneNumbers(contacts);
                sorter.printSorted(contacts);
            }
            case "Email" -> {
                sorter.sortByEmailUsingShellSort(contacts);
                sorter.printSorted(contacts);
            }
            case "Recently Added" -> {
                sorter.sortByRecentContact(contacts);
                sorter.printSorted(contacts);
            }
        }

        tableView.setItems(FXCollections.observableArrayList(contacts)); // populate the tableview with the sorted contacts linked list
    }
    public void debug(ActionEvent event) throws IOException {
        System.out.println("Latest contacts: ");
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            System.out.println(i + ". " + contact);
        }
        System.out.println("Favourites: ");
        for (int i = 0; i < favourites.size(); i++) {
            Favourite favourite = favourites.get(i);
            System.out.println(i + ". " + favourite);
        }
    }
    // ------------------------------------------Edit ---------------------------------

    public void editContact(ActionEvent event) throws IOException {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("EditContact.fxml"));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            if (!favouriteTableView.isVisible()){
                Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
                nameField.setText(selectedContact.getName());
                phoneField.setText(selectedContact.getPhone());
                emailField.setText(selectedContact.getEmail());
            }
            else {
                Favourite selectedFavourite = favouriteTableView.getSelectionModel().getSelectedItem();
                nameField.setText(selectedFavourite.getNameFav());
                phoneField.setText(selectedFavourite.getPhoneFav());
                emailField.setText(selectedFavourite.getEmailFav());
                // refresh the tableview
            }
            doneEditButton.setDisable(false);
            doneEditButton.setVisible(true);
            cancelEditButton.setDisable(false);
            cancelEditButton.setVisible(true);
            createContactButton.setVisible(false);
            uploadCreateContactImageButton.setVisible(false);
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (selectedContact == null) {
            return;
        }

    }
    public void doneEdit (ActionEvent event) throws IOException {
        if (!favouriteTableView.isVisible()){
            Contact selectedContact = tableView.getSelectionModel().getSelectedItem(); // get the selected contact
            selectedContact.setName(nameField.getText()); // set the selected contact's name to the text in the name field
            selectedContact.setPhone(phoneField.getText());
            selectedContact.setEmail(emailField.getText());
            // update contact in list
            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                if (contact.equals(selectedContact)) {
                    contacts.set(i, selectedContact);
                }
            }
        }
        else {
            Favourite selectedFavourite = favouriteTableView.getSelectionModel().getSelectedItem();
            selectedFavourite.setNameFav(nameField.getText());
            selectedFavourite.setPhoneFav(phoneField.getText());
            selectedFavourite.setEmailFav(emailField.getText());
            // update contact in favourites list
            for (int i = 0; i < favourites.size(); i++) {
                Favourite favourite = favourites.get(i);
                if (favourite.equals(selectedFavourite)) {
                    favourites.set(i, selectedFavourite);
                }
            }
            // update contact in contacts list
            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                if (contact.equals(selectedFavourite)) {
                    contact.setName(selectedFavourite.getNameFav());
                    contact.setPhone(selectedFavourite.getPhoneFav());
                    contact.setEmail(selectedFavourite.getEmailFav());
                }
            }

        }
        favouriteTableView.refresh();
        backToCreateContact();
    }

    public void cancelEdit (ActionEvent event) throws IOException {
        backToCreateContact();
    }
    public void backToCreateContact(){
        // clear text fields and hide Done and Cancel buttons
        clearTextFields();
        doneEditButton.setDisable(true);
        doneEditButton.setVisible(false);
        cancelEditButton.setDisable(true);
        cancelEditButton.setVisible(false);
        createContactButton.setVisible(true);
        uploadCreateContactImageButton.setVisible(true);
    }
    public void clearTextFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    // --------------------------------- Search ---------------------------------

    // Search using linked list
    public void searchContact2(ActionEvent event) throws IOException{
        searchContactsField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                String search = searchContactsField.getText();
                for (int i = 0; i < contacts.size(); i++) {
                    Contact contact = contacts.get(i);
                    if (contact.getName().equals(search)) {
                        System.out.println(contact);
                    }
                }
            }
        });
    }
    // search using tableview
    public void searchContact(ActionEvent event) throws IOException {
        FilteredList<Contact> filteredData = new FilteredList<>(FXCollections.observableList(contacts));
        tableView.setItems(filteredData);
        searchContactsField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(createPredicate(newValue))
        );

    }
    private Predicate<Contact> createPredicate(String searchText){ // create a predicate to filter the tableview
        return contact -> { // return true if the search text is empty or if the search text is found in the contact
            if (searchText == null || searchText.isEmpty()) return true; // if the search text is empty, return true
            return searchFindsOrder(contact, searchText); // return true if the search text is found in the contact
        };
    }
    private boolean searchFindsOrder(Contact contact, String searchText){ // return true if the search text is found in the contact
        return (contact.getName().toLowerCase().contains(searchText.toLowerCase())) || // return true if the search text is found in the contact
                (contact.getPhone().toLowerCase().contains(searchText.toLowerCase())) ||
                (contact.getEmail().toLowerCase().contains(searchText.toLowerCase()));
    }
    // when right-clicking on a contact, a context menu containing 'add to favorites' will appear to add the contact to the favorites linked list
    public void addToFavorites(ActionEvent event) throws IOException {
        Favourite selectedContact = tableView.getSelectionModel().getSelectedItem();


        if (selectedContact == null) {
            return;
        }
        // add only one time to favourites
        for (int i = 0; i < observableFavourites.size(); i++) {
            Favourite favourite = observableFavourites.get(i);
            if (favourite.equals(selectedContact)) {
                return;
            }
        }
        observableFavourites.add(selectedContact);
        favouriteTableView.setItems(observableFavourites);
        System.out.println("Added to favorites: " + selectedContact);
        System.out.println(observableFavourites);

    }
    public void viewFavorites(ActionEvent event) throws IOException {
        Favourite favourite;
        favouriteTableView.setVisible(true);
        favouriteTableView.setDisable(false);
        goBackButton.setVisible(true);
        goBackButton.setDisable(false);
        System.out.println("Favourites: ");
        for (int i = 0; i < observableFavourites.size(); i++) {
            favourite = observableFavourites.get(i);
            System.out.println(i + ". " + favourite);
        }
        System.out.println("favouriteTableView.isVisible(): " + favouriteTableView.isVisible());
        favouriteTableView.setItems(observableFavourites);
    }
    public void goBackToMain(ActionEvent event) throws IOException {
        favouriteTableView.setVisible(false);
        favouriteTableView.setDisable(true);
        goBackButton.setVisible(false);
        goBackButton.setDisable(true);

    }






}