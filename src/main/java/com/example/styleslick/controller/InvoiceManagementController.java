package com.example.styleslick.controller;

import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class InvoiceManagementController {


    @FXML
    private void executeShowAllInvoices() {

    }


    @FXML
    private void executeExitInvoiceManagement(){
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
    }



    @FXML
    private void onKeyPressedEnterShowAllInvoices(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAllInvoices();
        }
    }

    @FXML
    private void onKeyPressedEnterExitInvoiceManagement(KeyEvent event){
        if(event.getCode().toString().equals("ENTER")){
            executeExitInvoiceManagement();
        }
    }


    @FXML
    private void onMouseClickedShowAllInvoices(MouseEvent event) {
        executeShowAllInvoices();
    }
    @FXML
    private void onMouseClickedExitInvoiceManagement(MouseEvent event){
        executeExitInvoiceManagement();
    }
}
