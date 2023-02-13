# Contacts_Directory_Desktop_App_using_JavaFX
A Contacts Directory Desktop App developed for a data structure assignment. This project uses Linked List structure using Java Collection Framework (JCF) and designed with JavaFX Scene Builder.
With this app, you can easily add, edit, and remove contacts, as well as upload and remove contact photos.

## GUI
![alt text](https://github.com/AlifBrahim/Contacts_Directory_Desktop_App_using_JavaFX/blob/master/main%20interface.png)
![alt text](https://github.com/AlifBrahim/Contacts_Directory_Desktop_App_using_JavaFX/blob/master/Main%20GUI.png)
![alt text](https://github.com/AlifBrahim/Contacts_Directory_Desktop_App_using_JavaFX/blob/master/Create%20Contact%20GUI.png)
![alt text](https://github.com/AlifBrahim/Contacts_Directory_Desktop_App_using_JavaFX/blob/master/Edit%20Contact%20GUI.png)

## Features
- Add, edit, and remove contacts
- Upload and remove contact photos
- Sort contacts by name, phone number, email, and recently added
- Search for contacts by name or phone number

## Code Overview
This project is built with JavaFX and uses FXML files to define the user interface. The main class is MainApp, which extends the Application class and loads the FXML files using the FXMLLoader class.

The application logic is split into several classes, including Contact, ContactsApp, ContactsDirectoryController, CreateContactController, EditContactController, Favourite, and Sorter.

## Installation
1. Clone this repository to your local machine.
2. Ensure that you have the following installed:
- Java JDK 11 or later
- JavaFX 19 or later
- Gradle 7 or later
3. Run gradle run in the root directory of the project to launch the application.

## Usage
When you launch the app, you will see the main window with a table of list of contacts. To add a new contact, click the "Create Contact" button and enter the contact's information. To edit a contact, select the contact from the list and click the "Edit Contact" button. To remove a contact, select the contact from the list and click the "Remove Contact" button.

You can also sort the contacts by name by clicking the respective column header. To search for a contact, enter a name or phone number in the search box at the top of the window. Finally, you can upload and remove contact photos by clicking the "Upload Image" and "Remove Image" buttons, respectively.
