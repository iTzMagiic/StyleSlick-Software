package com.example.styleslick.controller.customerManagement;

import com.example.styleslick.model.Database;
import com.example.styleslick.model.Rules;
import com.example.styleslick.model.UserSession;
import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Map;

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

        Map<String, String> fields = new HashMap<>();

        fields.put("username", field_username.getText());
        fields.put("name", field_name.getText());
        fields.put("nachname", field_lastName.getText());
        fields.put("strasse", field_street.getText());
        fields.put("plz", field_plz.getText());
        fields.put("ort", field_ort.getText());
        fields.put("gekauft_ueber", field_platform.getText());

        Map<String, String> filledFields = Rules.getFilledFields(fields);
        if (!filledFields.isEmpty()) {

        }


//        String username = field_username.getText();
//        String name = field_name.getText();
//        String lastName = field_lastName.getText();
//        String street = field_street.getText();
//        String ort = field_ort.getText();
//        String platform = field_platform.getText();
//        String stringPlz = field_plz.getText();
//
//
//        if (stringPlz != null && !stringPlz.isEmpty()) {
//            try {
//                int plz = Integer.parseInt(stringPlz);
//                database.addCustomer(username, name, lastName, street, plz, ort, platform);
//            } catch (NumberFormatException e) {
//                System.err.println("Fehler beim konvertieren von String zu Integer. " + e.getMessage());
//            }
//        } else {
//            database.addCustomer(username, name, lastName, street, 0, ort, platform);
//        }

        field_username.clear();
        field_name.clear();
        field_lastName.clear();
        field_street.clear();
        field_ort.clear();
        field_platform.clear();
        field_plz.clear();
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
