package com.example.styleslick.controller;

import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class CustomerManagementController{



    @FXML
    private void executeAddCustomer() {
        SceneManager.switchScene("/com/example/styleslick/customerManagement/addCustomer-view.fxml", "Kunden hinzufügen");
    }
    @FXML
    private void onKeyPressedEnterAddCustomer(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeAddCustomer();
        }
    }
    @FXML
    private void onMouseClickedAddCustomer(MouseEvent event) {
        executeAddCustomer();
    }



    @FXML
    private void executeShowCustomers() {
        SceneManager.switchScene("/com/example/styleslick/customerManagement/showCustomers-view.fxml", "Kunden anzeigen");
    }
    @FXML
    private void onKeyPressedEnterShowCustomers(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeShowCustomers();
        }
    }
    @FXML
    private void onMouseClickedShowCustomers(MouseEvent event) {
        executeShowCustomers();
    }



    @FXML
    private void executeSearchCustomer() {
        SceneManager.switchScene("/com/example/styleslick/customerManagement/searchCustomer-view.fyml","Kunden Suchen");
    }
    @FXML
    private void onKeyPressedEnterSearchCustomer(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeSearchCustomer();
        }
    }
    @FXML
    private void onMouseClickedSearchCustomer(MouseEvent event) {
        executeSearchCustomer();
    }



    @FXML
    private void executeExitCustomerManagement(){
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
    }
    @FXML
    private void onKeyPressedEnterExitCustomerManagement(KeyEvent event){
        if(event.getCode().toString().equals("ENTER")){
            executeExitCustomerManagement();
        }
    }
    @FXML
    private void onMouseClickedExitCustomerManagement(MouseEvent event){
        executeExitCustomerManagement();
    }

}
