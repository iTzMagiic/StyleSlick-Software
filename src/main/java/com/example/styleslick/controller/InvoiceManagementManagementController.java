package com.example.styleslick.controller;

import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class InvoiceManagementManagementController {


    @FXML
    private void executeExitInvoiceManagement(){
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
    }
    @FXML
    private void onKeyPressedEnterExitInvoiceManagement(KeyEvent event){
        if(event.getCode().toString().equals("ENTER")){
            executeExitInvoiceManagement();
        }
    }
    @FXML
    private void onMouseClickedExitInvoiceManagement(MouseEvent event){
        executeExitInvoiceManagement();
    }
}
