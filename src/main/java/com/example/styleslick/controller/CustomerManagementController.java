package com.example.styleslick.controller;

import com.example.styleslick.utils.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CustomerManagementController{



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
