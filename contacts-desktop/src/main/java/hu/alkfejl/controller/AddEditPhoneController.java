package hu.alkfejl.controller;

import hu.alkfejl.model.Contact;
import hu.alkfejl.model.Phone;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEditPhoneController implements Initializable {

    private Stage stage;
    private Phone phone;
    private Contact contact;


    public void init(Stage stage, Phone phone, Contact contact) {
        this.stage = stage;
        this.phone = phone;
        this.contact = contact;

        number.textProperty().bindBidirectional(this.phone.numberProperty());
        phoneType.valueProperty().bindBidirectional(this.phone.phoneTypeProperty());

        if(phoneType.getSelectionModel().isEmpty()){
            phoneType.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private TextField number;

    @FXML
    private ComboBox<Phone.PhoneType> phoneType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        phoneType.getItems().setAll(Phone.PhoneType.values());
    }

    @FXML
    public void onCancel(){
        stage.close();
    }

    @FXML
    public void onSave(){
        contact.getPhones().remove(phone);
        contact.getPhones().add(phone);
        stage.close();
    }
}
