package com.example.styleslick.controller;


import com.example.styleslick.model.UserSession;
import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class LoggedInController{




    @FXML
    private void executeCustomerManagement() {
        SceneManager.switchScene("/com/example/styleslick/customerManagement-view.fxml", "Kundenverwaltung");
    }
    @FXML
    private void onKeyPressedEnterCustomerManagement(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeCustomerManagement();
        }
    }
    @FXML
    private void onMousePressedCustomerManagement(MouseEvent event) {
        executeCustomerManagement();
    }



    @FXML
    private void executeArticleManagement() {
        SceneManager.switchScene("/com/example/styleslick/articleManagement-view.fxml", "Kundenverwaltung");
    }
    @FXML
    private void onKeyPressedEnterArticleManagement(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeArticleManagement();
        }
    }
    @FXML
    private void onMousePressedArticleManagement(MouseEvent event) {
        executeArticleManagement();
    }



    @FXML
    private void executeOrderManagement() {
        SceneManager.switchScene("/com/example/styleslick/orderManagement-view.fxml", "Kundenverwaltung");
    }
    @FXML
    private void onKeyPressedEnterOrdereManagement(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeOrderManagement();
        }
    }
    @FXML
    private void onMousePressedOrderManagement(MouseEvent event) {
        executeOrderManagement();
    }



    @FXML
    private void executeSettings() {
        SceneManager.switchScene("/com/example/styleslick/settings-view.fxml", "Kundenverwaltung");
    }
    @FXML
    private void onKeyPressedEnterSettings(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeSettings();
        }
    }
    @FXML
    private void onMousePressedSettings(MouseEvent event) {
        executeSettings();
    }




    @FXML
    private void executeLogout() {
        SceneManager.switchScene("/com/example/styleslick/login-view.fxml", "Willkommen");

        UserSession session = UserSession.getInstance();
        session.clearSession();
    }
    @FXML
    private void onMouseClickedLogout(MouseEvent event) {
        executeLogout();
    }
    @FXML
    private void onKeyPressedEnterLogout(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeLogout();
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
