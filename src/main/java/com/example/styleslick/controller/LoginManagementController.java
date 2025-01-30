package com.example.styleslick.controller;

import com.example.styleslick.model.Database;
import com.example.styleslick.service.AlertService;
import com.example.styleslick.service.UserSession;
import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class LoginManagementController {

    @FXML
    private TextField field_username;
    @FXML
    private PasswordField field_password;


    public void initialize() {
        field_username.setText("styleslick");
        field_password.setText("rVHjMtqL{u%LZme=MQRHu~Q");
    }

    @FXML
    private void executeLogin() {
        String username = field_username.getText();
        String password = field_password.getText();
        Database database = new Database(username, password);

        if (!database.isConnected()) {
            AlertService.showErrorAlert("username oder Passwort falsch.");
            return;
        }

        UserSession session = UserSession.getInstance();
        session.setDatabase(database);

        SceneManager.switchScene("/com/example/styleslick/Home-view.fxml", "Willkommen");
    }


    @FXML
    private void executeExit() {
        System.exit(0);
    }


    @FXML
    private void onMouseClickedLogin(MouseEvent event) {
        executeLogin();
    }


    @FXML
    private void onKeyPressedEnterLogin(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeLogin();
        }
    }


    @FXML
    private void onMouseClickedExit(MouseEvent event) {
        executeExit();
    }


    @FXML
    private void onKeyPressedEnterExit(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExit();
        }
    }
}