package com.example.styleslick.controller.customerManagement;

import com.example.styleslick.model.Database;
import com.example.styleslick.model.UserSession;
import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AddCustomerController {

    UserSession userSession;

    @FXML
    private TextField field_username;
    @FXML
    private TextField field_name;
    @FXML
    private TextField field_lastName;
    @FXML
    private TextField field_street;
    @FXML
    private TextField field_plz;
    @FXML
    private TextField field_ort;
    @FXML
    private TextField field_platform;



    public void initialize() {
        userSession = UserSession.getInstance();
    }



    @FXML
    private void executeAddCustomer() {
        /* TODO: AddCustomer Methode ausbessern !
            - Rules Klasse neue Methoden für richtige eingaben um Kunden in die Datenbank zu schreiben
            - Prüfen ob der Kunde schon vorhanden ist
         */


        String username = field_username.getText();
        String name = field_name.getText();
        String lastName = field_lastName.getText();
        String street = field_street.getText();
        String stringPlz = field_plz.getText();
        int plz = Integer.parseInt(stringPlz);
        String ort = field_ort.getText();
        String platform = field_platform.getText();

        Database database = userSession.getDatabase();

        database.addCustomer(username, name, lastName, street, plz, ort, platform);
    }

    @FXML
    private void onKeyPressedEnterAddCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeAddCustomer();
        }
    }

    @FXML
    private void onMouseClickedAddCustomer(MouseEvent event) {
        executeAddCustomer();
    }



    @FXML
    private void executeExitAddCustomer() {
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen")
    }

    @FXML
    private void onKeyPressedEnterExitAddCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitAddCustomer();
        }
    }

    @FXML
    private void onMouseClickedExitAddCustomer(MouseEvent event) {
        executeExitAddCustomer();
    }

}
