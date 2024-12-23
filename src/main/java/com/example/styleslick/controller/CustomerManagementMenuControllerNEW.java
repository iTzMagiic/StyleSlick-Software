package com.example.styleslick.controller;

import com.example.styleslick.service.CustomerService;
import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class CustomerManagementMenuControllerNEW {




    @FXML
    private TableColumn<?, ?> column_lastName;

    @FXML
    private TableColumn<?, ?> column_name;

    @FXML
    private TableColumn<?, ?> column_ort;

    @FXML
    private TableColumn<?, ?> column_platform;

    @FXML
    private TableColumn<?, ?> column_plz;

    @FXML
    private TableColumn<?, ?> column_street;

    @FXML
    private TableColumn<?, ?> column_username;

    @FXML
    private TableView<?> tableView_customer;



    @FXML
    private void executeExitCustomerManagement(){
        CustomerService.getInstance().clearSession();
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
