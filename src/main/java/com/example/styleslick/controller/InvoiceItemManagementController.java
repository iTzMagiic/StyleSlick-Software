package com.example.styleslick.controller;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Invoice;
import com.example.styleslick.model.InvoiceItem;
import com.example.styleslick.service.AlertService;
import com.example.styleslick.service.ArticleService;
import com.example.styleslick.service.InvoiceService;
import com.example.styleslick.utils.SceneManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class InvoiceItemManagementController implements Initializable {

    private InvoiceService invoiceService;
    private ArticleService articleService;
    private Invoice invoice;


    @FXML
    private TableColumn<InvoiceItem, Integer> column_amount;
    @FXML
    private TableColumn<InvoiceItem, String> column_articleNumber;
    @FXML
    private TableColumn<InvoiceItem, String> column_name;
    @FXML
    private TableColumn<Article, Integer> column_article_amount;
    @FXML
    private TableColumn<Article, Integer> column_article_articleID;
    @FXML
    private TableColumn<Article, Integer> column_article_categoryID;
    @FXML
    private TableColumn<Article, String> column_article_color;
    @FXML
    private TableColumn<Article, String> column_article_manufacturer;
    @FXML
    private TableColumn<Article, String> column_article_name;
    @FXML
    private TableColumn<Article, Double> column_article_price;
    @FXML
    private TableColumn<Article, LocalDate> column_article_purchase_date;
    @FXML
    private TableColumn<Article, String> column_article_purchased_from;
    @FXML
    private TableColumn<Article, String> column_article_quality;
    @FXML
    private TableColumn<Article, Integer> column_article_stock;
    @FXML
    private TextField field_amount;
    @FXML
    private TextField field_articleNumber;
    @FXML
    private TableView<InvoiceItem> tableView_invoiceItem;
    @FXML
    private TableView<Article> tableView_articles;
    @FXML
    private Label label_invoiceNumber;
    @FXML
    private Label label_customerNumber;
    @FXML
    private Label label_date;


    public void initialize(URL location, ResourceBundle resources) {
        articleService = ArticleService.getInstance();
        invoiceService = InvoiceService.getInstance();
        invoice = invoiceService.getCurrentInvoice();

        LocalDate todayDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        label_date.setText(todayDate.format(formatter));

        executeShowAllItems();
    }


    private void executeShowAllItems() {
        List<InvoiceItem> listOfInvoiceItems;
        tableView_articles.setVisible(false);
        tableView_invoiceItem.setVisible(true);

        listOfInvoiceItems = invoiceService.getInvoiceItems(invoice.getInvoiceID());

        tableView_invoiceItem.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                field_articleNumber.setText(String.valueOf(tableView_invoiceItem.getSelectionModel().getSelectedItem().getArticleNumber()));
                field_amount.setText(String.valueOf(tableView_invoiceItem.getSelectionModel().getSelectedItem().getAmount()));
                tableView_invoiceItem.getSelectionModel().clearSelection();
            }
        });


        ObservableList<InvoiceItem> observableList = FXCollections.observableArrayList(listOfInvoiceItems);


        Task<Void> showAllTask = new Task<>() {

            @Override
            protected Void call() {

                Platform.runLater(() -> {
                    label_invoiceNumber.setText(invoice.getInvoiceNumber());
                    label_customerNumber.setText(invoice.getCustomerNumber());

                    column_articleNumber.setCellValueFactory(new PropertyValueFactory<>("articleNumber"));
                    column_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
                    column_name.setCellValueFactory(new PropertyValueFactory<>("articleName"));

                    tableView_invoiceItem.setItems(observableList);
                });

                return null;
            }

        };

        new Thread(showAllTask).start();
    }


    private void executeAddItem() {
        // Es wurde mit absicht eine Map erzeugt, für den Fall das sich die Artikel Attributen in der Datenbank verändern.
        Map<String, String> articleToAdd = new HashMap<>();

        if (field_articleNumber.getText().isEmpty()) {
            Article selectedArticle = tableView_articles.getSelectionModel().getSelectedItem();
            if (selectedArticle != null) {
                articleToAdd.put("article_number", selectedArticle.getArticleNumber());
            }
        } else {
            articleToAdd.put("article_number", field_articleNumber.getText());
        }

        articleToAdd.put("amount", field_amount.getText());


        if (invoiceService.addItemToInvoice(articleToAdd, invoice.getInvoiceID())) {
            executeShowAllItems();
            clearFields();
        }
    }


    private void executeUpdateItem() {

        InvoiceItem selectedItem = tableView_invoiceItem.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            AlertService.showErrorAlert("Bitte wählen Sie ein Artikel aus der Bestellung aus.");
            return;
        }

        if (field_amount.getText().isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie eine Menge an.");
            return;
        }

        if (invoiceService.updateItem(selectedItem, field_amount.getText())) {
            executeShowAllItems();
        }
    }


    private void executeDeleteItem() {

        if (tableView_invoiceItem.getSelectionModel().getSelectedItem() == null) {
            AlertService.showErrorAlert("Bitte wählen Sie ein Artikel aus einer Bestellung aus.");
            return;
        }

        InvoiceItem selectedItem = tableView_invoiceItem.getSelectionModel().getSelectedItem();
        tableView_invoiceItem.getSelectionModel().clearSelection();

        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich den Artikel: '" + selectedItem.getArticleName() +
                "' aus der Bestellung löschen?")) {
            return;
        }


        if (invoiceService.deleteArticleFromInvoice(selectedItem.getInvoiceItemID())) {


            if (AlertService.showConfirmAlertResult("Soll der Bestand wieder angepasst werden?")) {
                invoiceService.addBackDeletedItem(selectedItem.getArticleID(), selectedItem.getAmount());
            }

            executeShowAllItems();
        }
    }


    private void executeShowAllArticles() {
        tableView_invoiceItem.setVisible(false);
        tableView_articles.setVisible(true);
        tableView_invoiceItem.getSelectionModel().clearSelection();
        field_amount.clear();
        field_articleNumber.clear();

        tableView_articles.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                field_articleNumber.setText(String.valueOf(tableView_articles.getSelectionModel().getSelectedItem().getArticleNumber()));
                tableView_articles.getSelectionModel().clearSelection();
            }
        });

        ObservableList<Article> observableList = FXCollections.observableArrayList(articleService.getAllArticles());


        Task<Void> showAllArticlesTask = new Task<>() {

            @Override
            protected Void call() {

                Platform.runLater(() -> {
                    column_article_articleID.setCellValueFactory(new PropertyValueFactory<>("articleID"));
                    column_article_categoryID.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
                    column_article_name.setCellValueFactory(new PropertyValueFactory<>("name"));
                    column_article_color.setCellValueFactory(new PropertyValueFactory<>("color"));
                    column_article_price.setCellValueFactory(new PropertyValueFactory<>("price"));
                    column_article_purchase_date.setCellValueFactory(new PropertyValueFactory<>("purchase_date"));
                    column_article_manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
                    column_article_purchased_from.setCellValueFactory(new PropertyValueFactory<>("purchased_from"));
                    column_article_quality.setCellValueFactory(new PropertyValueFactory<>("quality"));
                    column_article_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
                    column_article_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));


                    tableView_articles.setItems(observableList);
                    tableView_articles.getSelectionModel().clearSelection();
                });

                return null;
            }
        };

        new Thread(showAllArticlesTask).start();
    }


    private void executeExit() {
        invoiceService.clearCurrentInvoice();
        SceneManager.switchScene("/com/example/styleslick/invoiceManagement-view.fxml", "Bestellung verwaltung", true);
    }


    @FXML
    private void onKeyPressedEnterShowAllArticles(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAllArticles();
        }
    }

    @FXML
    private void onKeyPressedEnterShowAllItems(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAllItems();
        }
    }

    @FXML
    private void onKeyPressedEnterAddItem(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeAddItem();
        }
    }

    @FXML
    private void onKeyPressedEnterDeleteItem(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeDeleteItem();
        }
    }

    @FXML
    private void onKeyPressedEnterExit(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExit();
        }
    }

    @FXML
    private void onKeyPressedEnterUpdateItem(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeUpdateItem();
        }
    }


    @FXML
    private void onMouseClickedShowAllArticles(MouseEvent event) {
        executeShowAllArticles();
    }

    @FXML
    private void onMouseClickedShowAllItems(MouseEvent event) {
        executeShowAllItems();
    }

    @FXML
    private void onMouseClickedAddItem(MouseEvent event) {
        executeAddItem();
    }

    @FXML
    private void onMouseClickedDeleteItem(MouseEvent event) {
        executeDeleteItem();
    }

    @FXML
    private void onMouseClickedExit(MouseEvent event) {
        executeExit();
    }

    @FXML
    private void onMouseClickedUpdateItem(MouseEvent event) {
        executeUpdateItem();
    }


    private void clearFields() {
        field_amount.clear();
        field_articleNumber.clear();
    }
}
