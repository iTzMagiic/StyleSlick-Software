package com.example.styleslick.controller;

import com.example.styleslick.model.Database;
import com.example.styleslick.service.AlertService;
import com.example.styleslick.service.UserSession;
import com.example.styleslick.utils.SceneManager;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    private TextField field_username;
    @FXML
    private PasswordField field_password;
    @FXML
    private JFXButton button_login;
    @FXML
    private JFXButton button_exit;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        field_username.setText("styleslick");
        field_password.setText("rVHjMtqL{u%LZme=MQRHu~Q");
    }


    private void executeLogin() {
        String username = field_username.getText();
        String password = field_password.getText();

        button_login.setMouseTransparent(true);

        // Erstelle den Task für den Login-Prozess
        Task<Database> loginTask = new Task<>() {

            @Override
            protected Database call() {
                Database database = new Database(username, password);
                return database.isConnected() ? database : null;
            }
        };

        // Erfolgreicher Login → UI-Update im JavaFX-Thread
        loginTask.setOnSucceeded(event -> {
            Database database = loginTask.getValue(); // Holt das Ergebnis aus call()
            if (database == null) {
                AlertService.showErrorAlert("Username oder Passwort falsch.");
            } else {
                UserSession session = UserSession.getInstance();
                session.setDatabase(database);
                SceneManager.switchScene("/com/example/styleslick/Home-view.fxml", "Willkommen", false);
            }
            button_login.setMouseTransparent(false);
        });

        // Falls etwas schiefgeht (Fehlermeldung)
        loginTask.setOnFailed(event -> {
            AlertService.showErrorAlert("Verbindungsfehler! Bitte erneut versuchen.");
            button_login.setMouseTransparent(false);
        });


        new Thread(loginTask).start();
    }


    private void executeExit() {

        button_exit.setMouseTransparent(true);

        Task<Void> exitTask = new Task<>() {

            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    System.exit(0);
                });

                return null;
            }
        };

        new Thread(exitTask).start();
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