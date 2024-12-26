package com.example.styleslick.controller;


import com.example.styleslick.service.*;
import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class LoggedInMenuController {

    @FXML
    private void executeCustomerManagement() {
        SceneManager.switchScene("/com/example/styleslick/customerManagement-view.fxml", "Kundenverwaltung");
        CustomerService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
    }


    @FXML
    private void executeArticleManagement() {
        SceneManager.switchScene("/com/example/styleslick/articleManagement-view.fxml", "Artikelverwaltung");
        ArticleService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
        CategoryService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
    }


    @FXML
    private void executeOrderManagement() {
        SceneManager.switchScene("/com/example/styleslick/orderManagement-view.fxml", "Bestellung verwaltung");
        OrderService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
    }


    @FXML
    private void executeSettings() {
        SceneManager.switchScene("/com/example/styleslick/settings-view.fxml", "Einstellungen");
    }


    @FXML
    private void executeLogout() {
        SceneManager.switchScene("/com/example/styleslick/login-view.fxml", "Willkommen");
        UserSession.getInstance().clearSession();
    }


    @FXML
    private void onKeyPressedEnterCustomerManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeCustomerManagement();
        }
    }


    @FXML
    private void onMousePressedCustomerManagement(MouseEvent event) {
        executeCustomerManagement();
    }


    @FXML
    private void onKeyPressedEnterArticleManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeArticleManagement();
        }
    }


    @FXML
    private void onMousePressedArticleManagement(MouseEvent event) {
        executeArticleManagement();
    }


    @FXML
    private void onKeyPressedEnterOrdereManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeOrderManagement();
        }
    }


    @FXML
    private void onMousePressedOrderManagement(MouseEvent event) {
        executeOrderManagement();
    }


    @FXML
    private void onKeyPressedEnterSettings(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeSettings();
        }
    }


    @FXML
    private void onMousePressedSettings(MouseEvent event) {
        executeSettings();
    }


    @FXML
    private void onMouseClickedLogout(MouseEvent event) {
        executeLogout();
    }


    @FXML
    private void onKeyPressedEnterLogout(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
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
        if (event.getCode().toString().equals("ENTER")) {
            executeExit();
        }
    }
}
