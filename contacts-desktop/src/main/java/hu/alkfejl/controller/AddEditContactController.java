package hu.alkfejl.controller;

import hu.alkfejl.App;
import hu.alkfejl.dao.ContactDAO;
import hu.alkfejl.dao.ContactDAOImpl;
import hu.alkfejl.dao.PhoneDAO;
import hu.alkfejl.dao.PhoneDAOImpl;
import hu.alkfejl.model.Contact;
import hu.alkfejl.model.Phone;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddEditContactController implements Initializable {

    private Contact contact;
    private PhoneDAO phoneDAO = new PhoneDAOImpl();
    private ContactDAO contactDAO = ContactDAOImpl.getInstance();

    @FXML
    private Button saveBtn;

    @FXML
    private TextField name;
    @FXML
    private TextField email;

    @FXML
    ListView<Phone> phones;

    @FXML
    private TextField address;
    @FXML
    private DatePicker dateOfBirth;
    @FXML
    private TextField company;
    @FXML
    private TextField position;
    
    @FXML
    private Label nameErrors;

    @FXML
    private Label emailErrors;

    public void setContact(Contact c) {
        this.contact = c;

        List<Phone> phonesList = phoneDAO.findAllByContactId(c.getId());
        contact.setPhones(FXCollections.observableArrayList(phonesList));

        name.textProperty().bindBidirectional(contact.nameProperty());
        email.textProperty().bindBidirectional(contact.emailProperty());
        phones.itemsProperty().bindBidirectional(contact.phonesProperty());
        address.textProperty().bindBidirectional(contact.addressProperty());
        dateOfBirth.valueProperty().bindBidirectional(contact.dateOfBirthProperty());
        company.textProperty().bindBidirectional(contact.companyProperty());
        position.textProperty().bindBidirectional(contact.positionProperty());

    }

    @FXML
    public void onCancel(){
        App.loadFXML("/fxml/main_window.fxml");
    }

    @FXML
    public void onSave(){
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                contact = contactDAO.save(contact);
                if(contact == null){
                    return false;
                }
                phoneDAO.deleteAll(contact.getId());

                contact.getPhones().forEach(phone -> {
                    phone.setId(0);
                    phoneDAO.save(phone, contact.getId());
                });
                return true;
            }
        };

        Thread updateThread = new Thread(task);
        updateThread.start();

        App.<MainWindowController>loadFXML("/fxml/main_window.fxml", App.getStage(), mainWindowController -> {
            task.setOnSucceeded(event -> {
                Boolean result = task.getValue();
                if(result) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Saving contact was successful", ButtonType.OK);
                    alert.showAndWait();
                    mainWindowController.refreshTable();

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Saving contact failed", ButtonType.OK);
                    alert.showAndWait();
                }
            });
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveBtn.disableProperty().bind(name.textProperty().isEmpty()
                .or(email.textProperty().isEmpty()
                        .or(dateOfBirth.valueProperty().isNull())));

        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && newValue.isEmpty()){
                nameErrors.setText("Name is required");
            }
            else{
                nameErrors.setText("");
            }
        });

        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && newValue.isEmpty()){
                emailErrors.setText("Email is required");
            } else if(newValue != null && !newValue.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){
                emailErrors.setText("Not a valid email");
            }
            else{
                emailErrors.setText("");
            }
        });


        phones.setCellFactory(param -> {
            ListCell<Phone> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem("Edit");
            MenuItem deleteItem = new MenuItem("Delete");

            contextMenu.getItems().addAll(editItem, deleteItem);

            editItem.setOnAction(event -> {
                Phone item = cell.getItem();
                showPhoneDialog(item);
            });
            deleteItem.setOnAction(event -> {
                contact.getPhones().remove(cell.getItem());
            });

            StringBinding cellTextBinding = new When(cell.itemProperty().isNotNull()).then(cell.itemProperty().asString()).otherwise("");
            cell.textProperty().bind(cellTextBinding);

            cell.emptyProperty().addListener((observable, wasEmpty, isNowEmpty) -> {
                if(isNowEmpty){
                    cell.setContextMenu(null);
                } else{
                    cell.setContextMenu(contextMenu);
                }

            });
            return cell;

        });
    }

    @FXML
    public void addNewPhone(){
        showPhoneDialog();
    }

    private void showPhoneDialog(Phone phone) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        App.<AddEditPhoneController>loadFXML("/fxml/add_edit_phone.fxml", stage, controller -> {
            controller.init(stage, phone, contact);
        });
        stage.showAndWait();
    }

    private void showPhoneDialog(){
        showPhoneDialog(new Phone());
    }
}
