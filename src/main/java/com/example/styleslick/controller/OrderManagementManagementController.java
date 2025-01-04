package com.example.styleslick.controller;

import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class OrderManagementManagementController {


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
