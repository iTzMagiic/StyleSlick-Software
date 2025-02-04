package com.example.styleslick.controller;

import com.example.styleslick.model.Database;
import com.example.styleslick.service.AlertService;
import com.example.styleslick.service.UserSession;
import com.example.styleslick.utils.SceneManager;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class LoginController {

    @FXML
    private TextField field_username;
    @FXML
    private PasswordField field_password;
    @FXML
    private JFXButton button_login;
    @FXML
    private JFXButton button_exit;


    public void initialize() {
        field_username.setText("styleslick");
        field_password.setText("rVHjMtqL{u%LZme=MQRHu~Q");
    }


    @FXML
    private void executeLogin() {
        String username = field_username.getText();
        String password = field_password.getText();

        button_login.setDisable(true); // Verhindert mehrfaches Klicken

        // Erstelle den Task für den Login-Prozess
        Task<Database> loginTask = new Task<>() {

            @Override
            protected Database call() {
                try {
                    // Simuliere eine Verzögerung damit der Button seine Animation komplett ausführt
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return null;
                }

                // Login-Logik
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
                SceneManager.switchScene("/com/example/styleslick/Home-view.fxml", "Willkommen");
            }
            button_login.setDisable(false); // Button wieder aktivieren
        });

        // Falls etwas schiefgeht (Fehlermeldung)
        loginTask.setOnFailed(event -> {
            AlertService.showErrorAlert("Verbindungsfehler! Bitte erneut versuchen.");
            button_login.setDisable(false);
        });

        // Task in separatem Thread starten
        new Thread(loginTask).start();
    }


    @FXML
    private void executeExit() {

        button_exit.setDisable(true);

        Task<Void> exitThread = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    button_exit.setDisable(false);
                    return null;
                }

                Platform.runLater(() -> {
                    System.exit(0);
                    button_exit.setDisable(false);
                });

                return null;
            }
        };

        new Thread(exitThread).start();
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