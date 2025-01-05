package com.example.styleslick.controller;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Category;
import com.example.styleslick.model.Customer;
import com.example.styleslick.service.ArticleService;
import com.example.styleslick.service.CategoryService;
import com.example.styleslick.service.RulesService;
import com.example.styleslick.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

//TODO:: Noch eine Möglichkeit ein Artikel zu bearbeiten.

public class ArticleManagementMenuController implements Initializable {

    private ArticleService articleService;

    @FXML
    private TableView<Article> tableView_articles;
    @FXML
    private TableColumn<Article, Integer> column_article_id;
    @FXML
    private TableColumn<Article, Integer> column_category_id;
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
        articleService = ArticleService.getInstance();
        CategoryService categoryService = CategoryService.getInstance();

        List<Category> listOfCategories = categoryService.getAllCategories();
        choiceBox_category_id.getItems().addAll(listOfCategories);

        // Beobachten, welche Zeile ausgewählt ist
        tableView_articles.getSelectionModel().selectedItemProperty();

        executeShowAllArticles();
    }


    private void executeShowAllArticles() {
        choiceBox_category_id.setValue(null);

        column_article_id.setCellValueFactory(new PropertyValueFactory<>("article_id"));
        column_category_id.setCellValueFactory(new PropertyValueFactory<>("category_id"));
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
    }


    private void executeAddArticle() {
        Map<String, String> fields = new HashMap<>();

        if (datePicker_purchase_date.getValue() == null) {
            RulesService.showErrorAlert("purchase_date darf nicht Leer sein.");
            return;
        }

        if (choiceBox_category_id.getValue() == null) {
            RulesService.showErrorAlert("Kategorie darf nicht Leer sein.");
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

        if (articleService.addArticle(fields)) {
            field_name.clear();
            field_color.clear();
            field_purchase_price.clear();
            field_manufacturer.clear();
            field_purchased_from.clear();
            field_quality.clear();
            field_amount.clear();
            field_stock.clear();
            executeShowAllArticles();
        }
    }


    private void executeUpdateArticle() {
        Map<String, String> fields = new HashMap<>();

        Article selectedArticle = tableView_articles.getSelectionModel().getSelectedItem();
        if (selectedArticle == null) {
            RulesService.showErrorAlert("Bitte wählen Sie einen Artikel aus der Tabelle aus, um ihn zu bearbeiten.");
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

        if (articleService.updateArticle(fields, selectedArticle.getArticle_id())) {
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
        }
    }


    private void executeDeleteArticle() {
        // Abrufen des ausgewählten Artikels
        Article selectedArticle = tableView_articles.getSelectionModel().getSelectedItem();
        if (selectedArticle == null) {
            RulesService.showErrorAlert("Bitte wählen Sie einen Artikel aus der Tabelle aus, um ihn zu löschen.");
            return;
        }

        int articleID = selectedArticle.getArticle_id(); // ID des Artikels

        // Artikel aus der Datenbank löschen
        if (articleService.deleteArticle(articleID)) {
            RulesService.showConfirmAlert("Artikel erfolgreich gelöscht.");
            executeShowAllArticles();
        } else {
            RulesService.showErrorAlert("Artikel wurde nicht gelöscht.");
        }
    }


    private void executeSearchArticle() {
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
            return;
        }

        ObservableList<Article> observableList = FXCollections.observableArrayList(listOfArticles);
        tableView_articles.setItems(observableList);
    }


    private void executeExitArticleManagement() {
        articleService.clearSession();
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
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
