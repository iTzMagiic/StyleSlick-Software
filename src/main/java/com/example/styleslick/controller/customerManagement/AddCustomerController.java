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
            - Rules Klasse neue Methoden für richtige eingaben um Kunden in die Datenbank zu schreiben und auf
            Datentypen achten vorallem auf Int !!
         */


        Database database = userSession.getDatabase();

        String username = field_username.getText();
        String name = field_name.getText();
        String lastName = field_lastName.getText();
        String street = field_street.getText();
        String ort = field_ort.getText();
        String platform = field_platform.getText();
        String stringPlz = field_plz.getText();
        if (stringPlz != null && !stringPlz.isEmpty()) {
            try {
                int plz = Integer.parseInt(stringPlz);
                database.addCustomer(username, name, lastName, street, plz, ort, platform);
            } catch (NumberFormatException e) {
                System.err.println("Fehler beim konvertieren von String zu Integer. " + e.getMessage());
            }
        } else {
            database.addCustomer(username, name, lastName, street, 0, ort, platform);
        }
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
        SceneManager.switchScene("/com/example/styleslick/customerManagement-view.fxml", "Kundenverwaltung");
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
