package com.example.styleslick.controller.customerManagement;

import com.example.styleslick.model.CustomerService;
import com.example.styleslick.model.UserSession;
import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SearchCustomerController {

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
    private TextField field_ort;
    @FXML
    private TextField field_plz;
    @FXML
    private TextField field_platform;


    public void initialize() {
         userSession = UserSession.getInstance();
    }



    @FXML
    private void executeSearchCustomer() {
        /* TODO: SearchCustomer Methode ausbessern !!
            - Prüfen wie viele eingaben getätigt wurden bsp. ob nur Name oder Name und Nachname eingegeben wurde.
            - CustomerService Methode aufrufen lassen um die Kunden zu finden.
         */
        String username = field_username.getText();
        String name = field_name.getText();
        String lastName = field_lastName.getText();
        String address = field_street.getText();
        String plz = field_plz.getText();
        String ort = field_ort.getText();
        String platform = field_platform.getText();


        CustomerService customerService = CustomerService.getInstance();
        //customerService.searchCustomer();
    }




    @FXML
    private void executeExitSearchCustomer() {
        SceneManager.switchScene("/com/example/styleslick/customerManagement-view.fxml", "Kundenverwaltung");
    }

    @FXML
    private void onKeyPressedEnterExitSearchCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitSearchCustomer();
        }
    }

    @FXML
    private void onMouseClickedExitSearchCustomer(MouseEvent event) {
        executeExitSearchCustomer();
    }

}
