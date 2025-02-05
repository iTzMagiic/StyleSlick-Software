package com.example.styleslick.controller;

import com.example.styleslick.model.Database;
import com.example.styleslick.service.AlertService;
import com.example.styleslick.service.UserSession;
import com.example.styleslick.utils.SceneManager;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

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


        // Erstelle den Task für den Login-Prozess
        Task<Database> loginTask = new Task<>() {

            @Override
            protected Database call() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("ERROR executeLogin() Thread.sleep() wurde Unterbrochen. FEHLER: {}", e.getMessage(), e);
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
        });

        // Falls etwas schiefgeht (Fehlermeldung)
        loginTask.setOnFailed(event -> {
            AlertService.showErrorAlert("Verbindungsfehler! Bitte erneut versuchen.");
        });


        new Thread(loginTask).start();
    }


    @FXML
    private void executeExit() {


        Task<Void> exitTask = new Task<>() {

            @Override
            protected Void call() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("ERROR executeExit() Thread.sleep() wurde Unterbrochen. FEHLER: {}", e.getMessage(), e);
                }

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