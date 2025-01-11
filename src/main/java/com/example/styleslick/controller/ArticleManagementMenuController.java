package com.example.styleslick.controller;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Category;
import com.example.styleslick.service.ArticleService;
import com.example.styleslick.service.CategoryService;
import com.example.styleslick.service.AlertService;
import com.example.styleslick.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;


public class ArticleManagementMenuController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(ArticleManagementMenuController.class);
    private ArticleService articleService;

    @FXML
    private TableView<Article> tableView_articles;
    @FXML
    private TableColumn<Article, Integer> column_articleID;
    @FXML
    private TableColumn<Article, Integer> column_categoryID;
    @FXML
    private TableColumn<Article, String> column_name;
    @FXML
    private TableColumn<Article, String> column_color;
    @FXML
    private TableColumn<Article, String> column_purchase_price;
    @FXML
    private TableColumn<Article, LocalDate> column_purchase_date;
    @FXML
    private TableColumn<Article, String> column_manufacturer;
    @FXML
    private TableColumn<Article, String> column_purchased_from;
    @FXML
    private TableColumn<Article, String> column_quality;
    @FXML
    private TableColumn<Article, Integer> column_amount;
    @FXML
    private TableColumn<Article, Integer> column_stock;
    @FXML
    private ChoiceBox<Category> choiceBox_category_id;
    @FXML
    private TextField field_name;
    @FXML
    private TextField field_color;
    @FXML
    private TextField field_purchase_price;
    @FXML
    private DatePicker datePicker_purchase_date;
    @FXML
    private TextField field_manufacturer;
    @FXML
    private TextField field_purchased_from;
    @FXML
    private TextField field_quality;
    @FXML
    private TextField field_amount;
    @FXML
    private TextField field_stock;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.debug("START initialize().");
        articleService = ArticleService.getInstance();
        CategoryService categoryService = CategoryService.getInstance();

        List<Category> listOfCategories = categoryService.getAllCategories();
        choiceBox_category_id.getItems().addAll(listOfCategories);

        // Beobachten, welche Zeile ausgewählt ist
        tableView_articles.getSelectionModel().selectedItemProperty();

        executeShowAllArticles();
        logger.debug("ENDE initialize() erfolgreich.\n\n");
    }


    private void executeShowAllArticles() {
        logger.debug("START executeShowAllArticles().");
        choiceBox_category_id.setValue(null);

        column_articleID.setCellValueFactory(new PropertyValueFactory<>("articleID"));
        column_categoryID.setCellValueFactory(new PropertyValueFactory<>("categoryID"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_color.setCellValueFactory(new PropertyValueFactory<>("color"));
        column_purchase_price.setCellValueFactory(new PropertyValueFactory<>("purchase_price"));
        column_purchase_date.setCellValueFactory(new PropertyValueFactory<>("purchase_date"));
        column_manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        column_purchased_from.setCellValueFactory(new PropertyValueFactory<>("purchased_from"));
        column_quality.setCellValueFactory(new PropertyValueFactory<>("quality"));
        column_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        column_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        ObservableList<Article> observableList = FXCollections.observableArrayList(articleService.getAllArticles());
        tableView_articles.setItems(observableList);
        logger.debug("ENDE executeShowAllArticles() erfolgreich.\n\n");
    }


    private void executeAddArticle() {
        logger.debug("START executeAddArticle().");
        Map<String, String> fields = new HashMap<>();

        if (datePicker_purchase_date.getValue() == null) {
            AlertService.showErrorAlert("Kaufdatum darf nicht Leer sein.");
            logger.warn("ENDE Benutzer hat kein Kaufdatum angegeben.\n\n");
            return;
        }

        if (choiceBox_category_id.getValue() == null) {
            AlertService.showErrorAlert("Kategorie darf nicht Leer sein.");
            logger.warn("ENDE Benutzer hat keine Kategorie angegeben.\n\n");
            return;
        }

        fields.put("category_id", String.valueOf(choiceBox_category_id.getValue().getID()));
        fields.put("name", field_name.getText());
        fields.put("color", field_color.getText());
        fields.put("purchase_price", field_purchase_price.getText());
        fields.put("purchase_date", datePicker_purchase_date.getValue().toString());
        fields.put("manufacturer", field_manufacturer.getText());
        fields.put("purchased_from", field_purchased_from.getText());
        fields.put("quality", field_quality.getText());
        fields.put("amount", field_amount.getText());
        fields.put("stock", field_stock.getText());

        if (!articleService.addArticle(fields)) {
            logger.warn("WARN Artikel wurde nicht in die Datenbank geschrieben.\n\n");
            return;
        }

        field_name.clear();
        field_color.clear();
        field_purchase_price.clear();
        field_manufacturer.clear();
        field_purchased_from.clear();
        field_quality.clear();
        field_amount.clear();
        field_stock.clear();
        executeShowAllArticles();
        logger.debug("ENDE executeAddArticle() erfolgreich.\n\n");
    }


    private void executeUpdateArticle() {
        logger.debug("START executeUpdateArticle().");
        Map<String, String> fields = new HashMap<>();

        Article selectedArticle = tableView_articles.getSelectionModel().getSelectedItem();
        if (selectedArticle == null) {
            AlertService.showErrorAlert("Bitte wählen Sie einen Artikel aus der Tabelle aus, um ihn zu bearbeiten.");
            logger.warn("WARN Benutzer hat kein Artikel aus der Tabelle ausgewählt.\n\n");
            return;
        }

        if (choiceBox_category_id.getValue() != null) {
            fields.put("category_id", String.valueOf(choiceBox_category_id.getValue().getID()));
        }

        fields.put("name", field_name.getText());
        fields.put("color", field_color.getText());
        fields.put("purchase_price", field_purchase_price.getText());
        if (datePicker_purchase_date.getValue() != null) {
            fields.put("purchase_date", datePicker_purchase_date.getValue().toString());
        }
        fields.put("manufacturer", field_manufacturer.getText());
        fields.put("purchased_from", field_purchased_from.getText());
        fields.put("quality", field_quality.getText());
        fields.put("amount", field_amount.getText());
        fields.put("stock", field_stock.getText());

        if (!articleService.updateArticle(fields, selectedArticle.getArticleID())) {
            logger.warn("WARN Artikel wurde nicht bearbeitet.\n\n");
            return;
        }

        choiceBox_category_id.setValue(null);
        field_name.clear();
        field_color.clear();
        field_purchase_price.clear();
        field_manufacturer.clear();
        field_purchased_from.clear();
        field_quality.clear();
        field_amount.clear();
        field_stock.clear();
        executeShowAllArticles();
        logger.debug("ENDE executeUpdateArticle() erfolgreich.\n\n");
    }


    private void executeDeleteArticle() {
        logger.debug("START executeDeleteArticle.");
        // Abrufen des ausgewählten Artikels
        Article selectedArticle = tableView_articles.getSelectionModel().getSelectedItem();
        if (selectedArticle == null) {
            AlertService.showErrorAlert("Bitte wählen Sie einen Artikel aus der Tabelle aus, um ihn zu löschen.");
            logger.warn("WARN Benutzer hat kein Artikel aus der Tabelle ausgewählt.\n\n");
            return;
        }

        int articleID = selectedArticle.getArticleID(); // ID des Artikels

        // Artikel aus der Datenbank löschen
        if (!articleService.deleteArticle(articleID)) {
            AlertService.showErrorAlert("Artikel wurde nicht gelöscht.");
            logger.warn("WARN Artikel wurde nicht gelöscht.\n\n");
            return;
        }

        AlertService.showConfirmAlert("Artikel erfolgreich gelöscht.");
        executeShowAllArticles();
        logger.debug("ENDE executeDeleteArticle() erfolgreich.\n\n");
    }


    private void executeSearchArticle() {
        logger.debug("START executeSearchArticle().");
        Map<String, String> fields = new HashMap<>();


        if (choiceBox_category_id.getValue() != null) {
            fields.put("category_id", String.valueOf(choiceBox_category_id.getValue().getID()));
        }

        fields.put("name", field_name.getText());
        fields.put("color", field_color.getText());
        fields.put("purchase_price", field_purchase_price.getText());
        if (datePicker_purchase_date.getValue() != null) {
            fields.put("purchase_date", datePicker_purchase_date.getValue().toString());
        }
        fields.put("manufacturer", field_manufacturer.getText());
        fields.put("purchased_from", field_purchased_from.getText());
        fields.put("quality", field_quality.getText());
        fields.put("amount", field_amount.getText());
        fields.put("stock", field_stock.getText());

        List<Article> listOfArticles = articleService.searchArticle(fields);

        if (listOfArticles == null || listOfArticles.isEmpty()) {
            choiceBox_category_id.setValue(null);
            field_name.clear();
            field_color.clear();
            field_purchase_price.clear();
            field_manufacturer.clear();
            field_purchased_from.clear();
            field_quality.clear();
            field_amount.clear();
            field_stock.clear();
            executeShowAllArticles();
            logger.warn("ENDE Es wurden keine Artikel gefunden.\n\n");
            return;
        }

        ObservableList<Article> observableList = FXCollections.observableArrayList(listOfArticles);
        tableView_articles.setItems(observableList);
        logger.debug("ENDE executeSearchArticle() erfolgreich.\n\n");
    }


    private void executeExitArticleManagement() {
        articleService.clearSession();
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
        logger.debug("BEENDET Benutzer hat ArticleManagementMenu Verlassen.--------------\n\n");
    }


    @FXML
    private void onKeyPressedEnterShowAllArticles(KeyEvent event) {
        if (event.getCode().toString().equals("Enter")) {
            executeShowAllArticles();
        }
    }


    @FXML
    private void onMouseClickedShowAllArticles(MouseEvent event) {
        executeShowAllArticles();
    }


    @FXML
    private void onKeyPressedUpdateArticle(KeyEvent event) {
        if (event.getCode().toString().equals("Enter")) {
            executeUpdateArticle();
        }
    }


    @FXML
    private void onMouseClickedUpdateArticle(MouseEvent event) {
        executeUpdateArticle();
    }


    @FXML
    void onKeyPressedDeleteArticle(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeDeleteArticle();
        }
    }


    @FXML
    void onKeyPressedEnterAddArticle(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeAddArticle();
        }
    }


    @FXML
    void onKeyPressedEnterSearchArticle(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeSearchArticle();
        }
    }


    @FXML
    void onMouseCLickedDeleteArticle(MouseEvent event) {
        executeDeleteArticle();
    }


    @FXML
    void onMouseClickedAddArticle(MouseEvent event) {
        executeAddArticle();
    }


    @FXML
    void onMouseClickedSearchArticle(MouseEvent event) {
        executeSearchArticle();
    }


    @FXML
    void onKeyPressedEnterExitArticleManagement(KeyEvent event) {
        if (event.getCode().toString().equals("Enter")) {
            executeExitArticleManagement();
        }
    }


    @FXML
    void onMouseClickedExitArticleManagement(MouseEvent event) {
        executeExitArticleManagement();
    }


}
