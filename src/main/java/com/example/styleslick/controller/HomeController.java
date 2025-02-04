package com.example.styleslick.controller;


import com.example.styleslick.model.Database;
import com.example.styleslick.service.*;
import com.example.styleslick.utils.SceneManager;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    UserSession userSession;
    @FXML
    private Label label_gesamtumsatz;
    @FXML
    private Label label_gesamtausgaben;
    @FXML
    private Label label_gewinn;
    @FXML
    private Label label_anzahlKunden;
    @FXML
    private Label label_date;
    @FXML
    private JFXButton button_article;

    @FXML
    private JFXButton button_category;

    @FXML
    private JFXButton button_customer;

    @FXML
    private JFXButton button_exit;

    @FXML
    private JFXButton button_invoice;

    @FXML
    private JFXButton button_logout;

    @FXML
    private JFXButton button_settings;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate todayDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        label_date.setText(todayDate.format(formatter));


        userSession = UserSession.getInstance();

        label_gesamtumsatz.setText(userSession.getDatabase().getTotalSales());
        label_gesamtausgaben.setText(userSession.getDatabase().getTotalExpenditure());
        label_gewinn.setText(userSession.getDatabase().getTotalProfit());
        label_anzahlKunden.setText(userSession.getDatabase().getTotalCustomer());
    }


    @FXML
    private void executeCustomerManagement() {

        button_customer.setDisable(true);

        Task<Void> customerThread = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    button_customer.setDisable(false);
                    return null;
                }

                Platform.runLater(() -> {
                    CustomerService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
                    SceneManager.switchScene("/com/example/styleslick/customerManagement-view.fxml", "Kundenverwaltung");
                    button_customer.setDisable(false);
                });

                return null;
            }
        };

        new Thread(customerThread).start();
    }


    @FXML
    private void executeCategoryManagement() {
        CategoryService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
        SceneManager.switchScene("/com/example/styleslick/categoryManagement-view.fxml", "Kategorie Verwaltung");
    }


    @FXML
    private void executeArticleManagement() {
        ArticleService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
        CategoryService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
        SceneManager.switchScene("/com/example/styleslick/articleManagement-view.fxml", "Artikelverwaltung");
    }


    @FXML
    private void executeInvoiceManagement() {
        InvoiceService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
        CustomerService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
        ArticleService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
        SceneManager.switchScene("/com/example/styleslick/invoiceManagement-view.fxml", "Bestellung verwaltung");
    }


    @FXML
    private void executeSettings() {
        SceneManager.switchScene("/com/example/styleslick/settings-view.fxml", "Einstellungen");
    }


    @FXML
    private void executeLogout() {
        UserSession.getInstance().clearSession();
        SceneManager.switchScene("/com/example/styleslick/login-view.fxml", "Einloggen");
    }


    @FXML
    private void onKeyPressedEnterCustomerManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeCustomerManagement();
        }
    }


    @FXML
    private void onKeyPressedEnterCategoryManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeCategoryManagement();
        }
    }


    @FXML
    private void onMousePressedCategoryManagement(MouseEvent event) {
        executeCategoryManagement();
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
            executeInvoiceManagement();
        }
    }


    @FXML
    private void onMousePressedOrderManagement(MouseEvent event) {
        executeInvoiceManagement();
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
