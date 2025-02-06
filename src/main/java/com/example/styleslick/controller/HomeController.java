package com.example.styleslick.controller;


import com.example.styleslick.service.*;
import com.example.styleslick.utils.SceneManager;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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


    private void executeCustomerManagement() {

        button_customer.setMouseTransparent(true);

        Task<Void> customerTask = new Task<>() {

            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    CustomerService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
                    SceneManager.switchScene("/com/example/styleslick/customerManagement-view.fxml", "Kundenverwaltung", true);
                    button_customer.setMouseTransparent(false);
                });

                return null;
            }
        };

        new Thread(customerTask).start();
    }


    private void executeCategoryManagement() {

        button_category.setMouseTransparent(true);

        Task<Void> categoryTask = new Task<>() {

            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    CategoryService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
                    SceneManager.switchScene("/com/example/styleslick/categoryManagement-view.fxml", "Kategorie Verwaltung", true);
                    button_category.setMouseTransparent(false);
                });

                return null;
            }
        };

        new Thread(categoryTask).start();
    }


    private void executeArticleManagement() {

        button_article.setMouseTransparent(true);

        Task<Void> articleTask = new Task<>() {

            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    ArticleService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
                    CategoryService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
                    SceneManager.switchScene("/com/example/styleslick/articleManagement-view.fxml", "Artikelverwaltung", true);
                    button_article.setMouseTransparent(false);
                });

                return null;
            }
        };

        new Thread(articleTask).start();
    }


    private void executeInvoiceManagement() {

        button_invoice.setMouseTransparent(true);

        Task<Void> invoiceTask = new Task<>() {

            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    InvoiceService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
                    CustomerService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
                    ArticleService.getInstance().setDatabase(UserSession.getInstance().getDatabase());
                    SceneManager.switchScene("/com/example/styleslick/invoiceManagement-view.fxml", "Bestellung verwaltung", true);
                    button_invoice.setMouseTransparent(false);
                });

                return null;
            }
        };


        new Thread(invoiceTask).start();
    }


    private void executeSettings() {

        button_settings.setMouseTransparent(true);

        Task<Void> settingsTask = new Task<>() {

            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    SceneManager.switchScene("/com/example/styleslick/settings-view.fxml", "Einstellungen", true);
                    button_settings.setMouseTransparent(false);
                });

                return null;
            }
        };

        new Thread(settingsTask).start();
    }


    private void executeLogout() {

        button_logout.setMouseTransparent(true);

        Task<Void> logoutTask = new Task<>() {

            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    UserSession.getInstance().clearSession();
                    SceneManager.switchScene("/com/example/styleslick/login-view.fxml", "Einloggen", false);
                    button_logout.setMouseTransparent(false);
                });

                return null;
            }
        };

        new Thread(logoutTask).start();
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
