package com.example.styleslick.controller.customerManagement;

import com.example.styleslick.model.Database;
import com.example.styleslick.model.UserSession;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AddCustomerController {

    UserSession userSession;



    public void initialize() {
        userSession = UserSession.getInstance();
    }



    @FXML
    private void executeAddCustomer() {
        /* TODO: AddCustomer Methode ausbessern !
            - Rules Klasse neue Methoden für richtige eingaben um Kunden in die Datenbank zu schreiben
            - Prüfen ob der Kunde schon vorhanden ist
         */


        String username = ""; // Textfield.getText();
        String name = "";
        String lastName = "";
        String adress = "";
        String plz = "";
        String platform = "";

        Database database = userSession.getDatabase();

        database.addCustomer(username, name, lastName, adress, plz, platform);
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

}
