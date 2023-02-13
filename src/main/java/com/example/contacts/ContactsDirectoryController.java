package com.example.contacts;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.Predicate;

public class ContactsDirectoryController {
    @FXML private SplitPane splitPane;
    @FXML private AnchorPane rightPane;
    @FXML private AnchorPane tableViewPane;
    @FXML private Button createContactButton;
    @FXML private Button viewFavouritesButton;
    @FXML private Button backFavouriteButton;
    @FXML private Button uploadCSVButton;
    @FXML private Button exportCSVButton;
    @FXML private Button deleteContactButton;
    @FXML private Button debugButton;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortByComboBox;
    @FXML private TableView<Contact> tableView;
    @FXML ContextMenu tableContextMenu;
    @FXML private TableColumn<Contact, String> nameCol;
    @FXML private TableColumn<Contact, String> phoneCol;
    @FXML private TableColumn<Contact, String> emailCol;
    @FXML private TableView<Favourite> tableFavView;
    @FXML private TableColumn<Favourite, String> nameFavCol;
    @FXML private TableColumn<Favourite, String> phoneFavCol;
    @FXML private TableColumn<Favourite, String> emailFavCol;
    @FXML private Label nameLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;
    @FXML private Label birthdayLabel;
    @FXML private Label notesLabel;
    @FXML private ImageView imageView;
    @FXML private Button editButton;
    private Image image;
    Sorter sorter = new Sorter();
    LinkedList<Contact> contacts = new LinkedList<>();
    LinkedList<Favourite> favourites = new LinkedList<>();

    private final Stage thisStage;
    public ContactsDirectoryController() {

        // Create the new stage
        thisStage = new Stage();

        // Load the FXML file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ContactsDirectory.fxml"));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            thisStage.setScene(new Scene(loader.load()));

            // Setup the window/stage
            thisStage.setTitle("Contacts");
            thisStage.getIcons().add(new Image("C:\\Users\\DELL\\IdeaProjects\\Contacts\\src\\main\\resources\\com\\example\\contacts\\contacts_google_icon.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Show the stage that was loaded in the constructor
     */
    public void showStage() {
        thisStage.showAndWait();
    }

    @FXML public void initialize() {
        // Set the table columns
        nameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("email"));
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        nameFavCol.setCellValueFactory(new PropertyValueFactory<Favourite, String>("name"));
        phoneFavCol.setCellValueFactory(new PropertyValueFactory<Favourite, String>("phone"));
        emailFavCol.setCellValueFactory(new PropertyValueFactory<Favourite, String>("email"));
        nameFavCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        phoneFavCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        emailFavCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        sortByComboBox.getItems().addAll("Name", "Phone", "Email", "Recently Added"); // add items to the combo box
        // Set the table items
//        tableView.setItems(contacts);
        tableView.setItems(FXCollections.observableList(contacts));

        CreateContactController createContactController = new CreateContactController(this);

        createContactButton.setOnAction(e -> {
            try {
                openCreateContact();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        // when user clicks on viewFavorites button, the table view will be replaced with the tableFavView
        viewFavouritesButton.setOnAction(e -> {
            tableFavView.setVisible(true);
            backFavouriteButton.setVisible(true);
            System.out.println("Is tableFavView visible? " + tableFavView.isVisible());
            System.out.println("Is tableFavView disabled? " + tableFavView.isDisabled());
        });

        backFavouriteButton.setOnAction(e -> {
            tableFavView.setVisible(false);
            backFavouriteButton.setVisible(false);
        });

        tableContextMenu.setOnAction(e -> {
            try {
                addToFavourites();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        uploadCSVButton.setOnAction(e -> {
            uploadCSV();
        });
        exportCSVButton.setOnAction(e -> {
            try {
                exportCSV();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        deleteContactButton.setOnAction(e -> {
            try {
                deleteContact();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        debugButton.setOnAction(e -> {
            try {
                debug();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        searchField.setOnAction(e -> {
            try {
                searchContact();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        sortByComboBox.setOnAction(e -> {
            try {
                sortBy();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        editButton.setOnAction(e -> {
            try {
                openEditContact();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
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
                    // update the image, name, phone and email labels
                    nameLabel.setText(rowData.getName());
                    phoneLabel.setText(rowData.getPhone());
                    emailLabel.setText(rowData.getEmail());
                    addressLabel.setText(rowData.getAddress());
                    birthdayLabel.setText(rowData.getBirthday());
                    notesLabel.setText(rowData.getNotes());

                    // when user clicks on other row, the image will be cleared or updated if the next row has imagePath
                    if (rowData.getImagePath() != null) {
                        // if the image is invalid URL or resource not found, the image will be cleared
                        image = new Image(String.valueOf(rowData.getImagePath()));
                        imageView.setImage(image);
                        nameLabel.setText(rowData.getName());
                        phoneLabel.setText(rowData.getPhone());
                        emailLabel.setText(rowData.getEmail());
                    }
                    else if (rowData.getName() != null || rowData.getPhone() != null || rowData.getEmail() != null){
                        imageView.setImage(null);
                        nameLabel.setText(rowData.getName());
                        phoneLabel.setText(rowData.getPhone());
                        emailLabel.setText(rowData.getEmail());
                    }
                    else {
                        imageView.setImage(null);
                        nameLabel.setText(null);
                        phoneLabel.setText(null);
                        emailLabel.setText(null);
                    }
                }
            });
            return row;
        });

        tableFavView.setRowFactory(tv -> { // when user clicks on a row, the image will be displayed
            TableRow<Favourite> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    Favourite rowData = row.getItem();
                    System.out.println("Clicked on: " + rowData.getName());
                    System.out.println("Image Path: " + rowData.getImagePath());
                    System.out.println();
                    // update the image, name, phone and email labels
                    nameLabel.setText(rowData.getName());
                    phoneLabel.setText(rowData.getPhone());
                    emailLabel.setText(rowData.getEmail());
                    addressLabel.setText(rowData.getAddress());
                    birthdayLabel.setText(rowData.getBirthday());
                    notesLabel.setText(rowData.getNotes());

                    // when user clicks on other row, the image will be cleared or updated if the next row has imagePath
                    if (rowData.getImagePath() != null) {
                        image = new Image(String.valueOf(rowData.getImagePath()));
                        imageView.setImage(image);
                        nameLabel.setText(rowData.getName());
                        phoneLabel.setText(rowData.getPhone());
                        emailLabel.setText(rowData.getEmail());
                    }
                    else if (rowData.getName() != null || rowData.getPhone() != null || rowData.getEmail() != null){
                        imageView.setImage(null);
                        nameLabel.setText(rowData.getName());
                        phoneLabel.setText(rowData.getPhone());
                        emailLabel.setText(rowData.getEmail());
                    }
                    else {
                        imageView.setImage(null);
                        nameLabel.setText(null);
                        phoneLabel.setText(null);
                        emailLabel.setText(null);
                    }
                }
            });
            return row;
        });

        // listen for changes in labels and update the table
        nameLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
            selectedContact.setName(newValue);
            Favourite selectedFavourite = tableFavView.getSelectionModel().getSelectedItem();
            // if selectedFavorite is not null, update the name in the favourite table
            if (selectedFavourite != null){
                selectedFavourite.setName(newValue);
            }
        });
        phoneLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
            selectedContact.setPhone(newValue);
            Favourite selectedFavourite = tableFavView.getSelectionModel().getSelectedItem();
            if (selectedFavourite != null){
                selectedFavourite.setPhone(newValue);
            }
        });
        emailLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
            selectedContact.setEmail(newValue);
            Favourite selectedFavourite = tableFavView.getSelectionModel().getSelectedItem();
            if (selectedFavourite != null){
                selectedFavourite.setEmail(newValue);
            }
        });
        addressLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
            selectedContact.setAddress(newValue);
            Favourite selectedFavourite = tableFavView.getSelectionModel().getSelectedItem();
            if (selectedFavourite != null){
                selectedFavourite.setAddress(newValue);
            }
        });
        birthdayLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
            selectedContact.setBirthday(newValue);
            Favourite selectedFavourite = tableFavView.getSelectionModel().getSelectedItem();
            if (selectedFavourite != null){
                selectedFavourite.setBirthday(newValue);
            }
        });
        notesLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
            selectedContact.setNotes(newValue);
            Favourite selectedFavourite = tableFavView.getSelectionModel().getSelectedItem();
            if (selectedFavourite != null){
                selectedFavourite.setNotes(newValue);
            }
        });
//        // listen for changes in the image and update the table
//        imageView.imageProperty().addListener((observable, oldValue, newValue) -> {
//            Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
//            // get absolute path of the image
//            String imagePath = newValue.getUrl();
//            selectedContact.setImagePath(imagePath);
//            // if the image is null, set the imagePath to null
//        });

        // listen for changes in the image and update the table if the image is not null
        imageView.imageProperty().addListener((observable, oldValue, newValue) -> {
            Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
            Favourite selectedFavourite = tableFavView.getSelectionModel().getSelectedItem();
            if (newValue != null) {
                // get absolute path of the image
                String imagePath = newValue.getUrl();
                selectedContact.setImagePath(imagePath);
                if (selectedFavourite != null){
                    selectedFavourite.setImagePath(imagePath);
                }
            }
            else {
                selectedContact.setImagePath(null);
                if (selectedFavourite != null){
                    selectedFavourite.setImagePath(null);
                }
            }
        });


    }

    public void openCreateContact() throws IOException {
        CreateContactController createContactController = new CreateContactController(this);
        createContactController.showStage();
    }

//    public void backToContacts() throws IOException {
//        FavouriteTableViewController favouriteTableViewController = new FavouriteTableViewController(this);
//        favouriteTableViewController.closeStage();
//        backFavouriteButton.setVisible(false);
//    }

    public void deleteContact() throws IOException {
        // get the selected contact
        Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
        // remove contact from table
        tableView.getItems().remove(selectedContact);
        // remove contact from list and remove from favourite list if it is a favourite
        contacts.remove(selectedContact);
        System.out.println("selectedContact: " + selectedContact);

        favourites.removeIf(favourite -> favourite.getName().equals(selectedContact.getName()) && favourite.getPhone().equals(selectedContact.getPhone()));


        // if tableFavView is visible, remove the contact from the favourite table
        if (tableFavView.isVisible()){
            Favourite selectedFavourite = tableFavView.getSelectionModel().getSelectedItem();
            tableFavView.getItems().remove(selectedFavourite);
            favourites.remove(selectedFavourite);
        }

        // TODO: remove favourite from list
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
                    String address = contact[3];
                    String birthday = contact[4];
                    String notes = contact[5];
                    String image = contact[6];
                    // add data to the contacts linked list
                    contacts.add(new Contact(name, phone, email, address, birthday, notes, image));
                }
                // populate the tableview with the contacts linked list
                tableView.setItems(FXCollections.observableArrayList(contacts));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void sortBy () throws IOException{
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
    public void debug() throws IOException {
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
        tableView.setItems(FXCollections.observableArrayList(contacts));
        tableView.refresh();
        tableFavView.setItems(FXCollections.observableArrayList(favourites));
        tableFavView.refresh();
    }
    // --------------------------------- Edit ---------------------------------
    // show contact info first when double clicking on a contact

    public void openEditContact() throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() == null) return; // if no contact is selected, return
        EditContactController editContactController = new EditContactController(this);
        editContactController.showStage();
    }
    

    // --------------------------------- Search ---------------------------------
    public void searchContact() throws IOException {
        FilteredList<Contact> filteredData = new FilteredList<>(FXCollections.observableList(contacts));
        tableView.setItems(filteredData);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
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

    // --------------------------------- Add to Favourites ---------------------------------
    public void addToFavourites() throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() == null) return; // if no contact is selected, return
        for (int i = 0; i < favourites.size(); i++) {
            Favourite favourite = favourites.get(i);
            if (favourite.getName().equals(tableView.getSelectionModel().getSelectedItem().getName())) return; // if the contact is already in the favourites, return
        }
        Contact contact = tableView.getSelectionModel().getSelectedItem(); // get the selected contact
        Favourite favourite = new Favourite(contact.getName(), contact.getPhone(), contact.getEmail(), contact.getAddress(), contact.getBirthday(), contact.getNotes(), contact.getImagePath()); // create a favourite object
        favourites.add(favourite); // add the favourite object to the favourites linked list
        tableFavView.setItems(FXCollections.observableArrayList(favourites)); // populate the tableview with the favourites linked list
    }
//    public void addToFavourite2s() throws IOException {
//        Favourite selectedContact = (Favourite) tableView.getSelectionModel().getSelectedItem();
//        if (selectedContact != null) {
//            // add only one time to favourites
//            for (int i = 0; i < favourites.size(); i++) {
//                Favourite favourite = favourites.get(i);
//                if (favourite.equals(selectedContact)) {
//                    return;
//                }
//            }
//            favourites.add(selectedContact);
//            tableFavView.setItems(FXCollections.observableArrayList(favourites));
//            System.out.println("Added to favourites: " + selectedContact);
//            System.out.println("Favourites: " + favourites);
//        }
//    }
    public void exportCSV() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (file != null) {
            try (PrintWriter pw = new PrintWriter(file)) {
                contacts.forEach(contact -> pw.println(contact.toCSV()));
            }
        }
    }


    public Label getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(Label nameLabel) {
        this.nameLabel = nameLabel;
    }

    public Label getPhoneLabel() {
        return phoneLabel;
    }

    public void setPhoneLabel(Label phoneLabel) {
        this.phoneLabel = phoneLabel;
    }

    public Label getEmailLabel() {
        return emailLabel;
    }

    public void setEmailLabel(Label emailLabel) {
        this.emailLabel = emailLabel;
    }

    public Label getAddressLabel() {
        return addressLabel;
    }

    public void setAddressLabel(Label addressLabel) {
        this.addressLabel = addressLabel;
    }

    public Label getBirthdayLabel() {
        return birthdayLabel;
    }

    public void setBirthdayLabel(Label birthdayLabel) {
        this.birthdayLabel = birthdayLabel;
    }

    public Label getNotesLabel() {
        return notesLabel;
    }

    public void setNotesLabel(Label notesLabel) {
        this.notesLabel = notesLabel;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setRightPane(AnchorPane rightPane) {
        this.rightPane = rightPane;
    }

    public AnchorPane getTableViewPane() {
        return tableViewPane;
    }

    public void setTableViewPane(AnchorPane tableViewPane) {
        this.tableViewPane = tableViewPane;
    }

    public LinkedList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(LinkedList<Contact> contacts) {
        this.contacts = contacts;
    }

    public LinkedList<Favourite> getFavourites() {
        return favourites;
    }

    public void setFavourites(LinkedList<Favourite> favourites) {
        this.favourites = favourites;
    }

    public SplitPane getSplitPane() {
        return splitPane;
    }

    public void setSplitPane(SplitPane splitPane) {
        this.splitPane = splitPane;
    }

    public Button getCreateContactButton() {
        return createContactButton;
    }

    public void setCreateContactButton(Button createContactButton) {
        this.createContactButton = createContactButton;
    }

    public Button getViewFavouritesButton() {
        return viewFavouritesButton;
    }

    public void setViewFavouritesButton(Button viewFavouritesButton) {
        this.viewFavouritesButton = viewFavouritesButton;
    }

    public Button getUploadCSVButton() {
        return uploadCSVButton;
    }

    public void setUploadCSVButton(Button uploadCSVButton) {
        this.uploadCSVButton = uploadCSVButton;
    }

    public Button getDeleteContactButton() {
        return deleteContactButton;
    }

    public void setDeleteContactButton(Button deleteContactButton) {
        this.deleteContactButton = deleteContactButton;
    }

    public Button getDebugButton() {
        return debugButton;
    }

    public void setDebugButton(Button debugButton) {
        this.debugButton = debugButton;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public void setSearchField(TextField searchField) {
        this.searchField = searchField;
    }

    public ComboBox getSortByComboBox() {
        return sortByComboBox;
    }

    public void setSortByComboBox(ComboBox sortByComboBox) {
        this.sortByComboBox = sortByComboBox;
    }

    public TableView getTableView() {
        return tableView;
    }

    public void setTableView(TableView tableView) {
        this.tableView = tableView;
    }

    public TableColumn getNameCol() {
        return nameCol;
    }

    public void setNameCol(TableColumn nameCol) {
        this.nameCol = nameCol;
    }

    public TableColumn getPhoneCol() {
        return phoneCol;
    }

    public void setPhoneCol(TableColumn phoneCol) {
        this.phoneCol = phoneCol;
    }

    public TableColumn getEmailCol() {
        return emailCol;
    }

    public void setEmailCol(TableColumn emailCol) {
        this.emailCol = emailCol;
    }

    public Stage getThisStage() {
        return thisStage;
    }
}
