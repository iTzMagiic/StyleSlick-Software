package com.example.styleslick.controller;

import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class OrderManagementMenuController {



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
    private void executeShowCustomer() {
        SceneManager.switchScene("/com/example/styleslick/customerManagement/showCustomer-view.fxml", "Kunden anzeigen");
    }
    @FXML
    private void onKeyPressedEnterShowCustomer(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeShowCustomer();
        }
    }
    @FXML
    private void onMouseClickedShowCustomer(MouseEvent event) {
        executeShowCustomer();
    }



    @FXML
    private void executeSearchCustomer() {
        SceneManager.switchScene("/com/example/styleslick/customerManagement/showCustomer-view.fxml", "Kunden suchen");
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
    private void executeExitOrderManagement(){
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
    }
    @FXML
    private void onKeyPressedEnterExitOrderManagement(KeyEvent event){
        if(event.getCode().toString().equals("ENTER")){
            executeExitOrderManagement();
        }
    }
    @FXML
    private void onMouseClickedExitOrderManagement(MouseEvent event){
        executeExitOrderManagement();
    }
}
