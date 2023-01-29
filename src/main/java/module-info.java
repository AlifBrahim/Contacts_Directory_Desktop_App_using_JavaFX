module com.example.contacts {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.contacts to javafx.fxml;
    exports com.example.contacts;

    opens com.example.contacts.demoCommunicationBtwinControllers to javafx.fxml;
    exports com.example.contacts.demoCommunicationBtwinControllers;
}