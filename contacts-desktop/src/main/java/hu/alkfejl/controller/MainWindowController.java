package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.dao.ContactDAO;
import hu.alkfejl.dao.ContactDAOImpl;
import hu.alkfejl.dao.PhoneDAO;
import hu.alkfejl.dao.PhoneDAOImpl;
import hu.alkfejl.model.Contact;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {

    private ContactDAO dao = ContactDAOImpl.getInstance();
    private PhoneDAO phoneDAO = new PhoneDAOImpl();
    private List<Contact> all;

    @FXML
    private TableView<Contact> contactTable;

    @FXML
    private TableColumn<Contact, String> nameColumn;

    @FXML
    private TableColumn<Contact, String> emailColumn;

    @FXML
    private TableColumn<Contact, Void> actionsColumn;

    @FXML
    private TextField nameSearch;

    @FXML
    private TextField emailSearch;

    @FXML
    public void onSearch(){
        List<Contact> filtered = all.stream().filter(contact -> contact.getName().contains(nameSearch.getText()) && contact.getEmail().contains(emailSearch.getText())).collect(Collectors.toList());
        contactTable.getItems().setAll(filtered);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshTable();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        actionsColumn.setCellFactory(param -> new TableCell<>(){

            private final Button deleteBtn = new Button("Delete");
            private final Button editBtn = new Button("Edit");

            {
                deleteBtn.setOnAction(event -> {
                    Contact c = getTableRow().getItem();
                    deleteContact(c);
                    refreshTable();
                });

                editBtn.setOnAction(event -> {
                    Contact c = getTableRow().getItem();
                    editContact(c);
                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                }
                else{
                    HBox container = new HBox();
                    container.getChildren().addAll(editBtn, deleteBtn);
                    container.setSpacing(10.0);
                    setGraphic(container);
                }
            }
        });


    }

    private void editContact(Contact c) {
        FXMLLoader fxmlLoader = App.loadFXML(("/fxml/add_edit_contact.fxml"));
        AddEditContactController controller = fxmlLoader.getController();
        controller.setContact(c);

    }

    private void deleteContact(Contact c) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete contact: " + c.getName(), ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(buttonType -> {
            if(buttonType.equals(ButtonType.YES)){

                Task<Void> deleteTask = new Task<>() {
                    @Override
                    protected Void call() {
                        phoneDAO.deleteAll(c.getId());
                        dao.delete(c);
                        return null;
                    }
                };

                deleteTask.setOnSucceeded(event1 -> refreshTable());

                Thread deleteThread = new Thread(deleteTask);
                deleteThread.start();

            }
        });
    }



    public void refreshTable() {
        all = dao.findAll();
        contactTable.getItems().setAll(all);
    }


    @FXML
    public void onExit(){
        Platform.exit();
    }

    @FXML
    public void onAddNewContact(){
        FXMLLoader fxmlLoader = App.loadFXML(("/fxml/add_edit_contact.fxml"));
        AddEditContactController controller = fxmlLoader.getController();
        controller.setContact(new Contact());
    }
}
