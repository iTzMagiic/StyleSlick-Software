package com.example.styleslick.controller;

import com.example.styleslick.model.CustomerService;
import com.example.styleslick.model.Database;
import com.example.styleslick.model.Rules;
import com.example.styleslick.model.UserSession;
import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    private TextField field_username;
    @FXML
    private TextField field_password;




    @FXML
    private void executeLogin() {
        String username = field_username.getText();
        String password = field_password.getText();
        Database database = new Database(username, password);

        if(!database.testConnection()) {
            Rules.showErrorAlert("Benutzername oder Passwort falsch.");
            return;
        }

        UserSession session = UserSession.getInstance();
        session.setDatabase(database);

        CustomerService customerService = CustomerService.getInstance();
        customerService.setDatabase(database);

        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
    }

    @FXML
    private void onMouseClickedLogin(MouseEvent event) {
        executeLogin();
    }

    @FXML
    private void onKeyPressedEnterLogin(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")){
            executeLogin();
        }
    }



    @FXML
    private void executeExit() {
        System.exit(0);
    }
    @FXML
    private void onMouseClickedExit(MouseEvent event) {
        executeExit();
    }
    @FXML
    private void onKeyPressedEnterExit(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeExit();
        }
    }




}