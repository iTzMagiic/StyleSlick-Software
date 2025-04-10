package com.example.styleslick.controller;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Category;
import com.example.styleslick.service.AlertService;
import com.example.styleslick.service.ArticleService;
import com.example.styleslick.service.CategoryService;
import com.example.styleslick.utils.SceneManager;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class ArticleManagementController implements Initializable {

    private ArticleService articleService;
    private CategoryService categoryService;

    @FXML
    private TableView<Article> tableView_articles;
    @FXML
    private TableColumn<Article, Integer> column_articleNumber;
    @FXML
    private TableColumn<Article, Integer> column_categoryName;
    @FXML
    private TableColumn<Article, String> column_name;
    @FXML
    private TableColumn<Article, String> column_color;
    @FXML
    private TableColumn<Article, String> column_price;
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
    private ChoiceBox<Category> choiceBox_categories;
    @FXML
    private TextField field_name;
    @FXML
    private TextField field_color;
    @FXML
    private TextField field_price;
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
    @FXML
    private TextField field_articleNumber;
    @FXML
    private JFXButton button_showAll;
    @FXML
    private JFXButton button_search;
    @FXML
    private JFXButton button_showAvaliableArticle;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        articleService = ArticleService.getInstance();
        categoryService = CategoryService.getInstance();

        List<Category> listOfCategories = categoryService.getAllCategories();
        choiceBox_categories.getItems().addAll(listOfCategories);


        tableView_articles.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Article doubleClickedArticle = tableView_articles.getSelectionModel().getSelectedItem();
                choiceBox_categories.setValue(listOfCategories.stream().filter(category -> category.getID() == doubleClickedArticle.getCategoryID()).findFirst().orElse(null));
                field_articleNumber.setText(doubleClickedArticle.getArticleNumber());
                field_name.setText(doubleClickedArticle.getName());
                field_color.setText(doubleClickedArticle.getColor());
                field_price.setText(String.valueOf(doubleClickedArticle.getPrice()));
                datePicker_purchase_date.setValue(doubleClickedArticle.getPurchase_date());
                field_manufacturer.setText(doubleClickedArticle.getManufacturer());
                field_purchased_from.setText(doubleClickedArticle.getPurchased_from());
                field_quality.setText(doubleClickedArticle.getQuality());
                field_amount.setText(String.valueOf(doubleClickedArticle.getAmount()));
                field_stock.setText(String.valueOf(doubleClickedArticle.getStock()));
            }
        });

        executeShowAllArticles();
    }


    private void executeShowAllArticles() {
        ObservableList<Article> observableList = FXCollections.observableArrayList(articleService.getAllArticles());

        button_showAll.setMouseTransparent(true);

        Task<Void> showAllTask = new Task<>() {

            @Override
            protected Void call() {

                Platform.runLater(() -> {
                    clearFields();

                    column_articleNumber.setCellValueFactory(new PropertyValueFactory<>("articleNumber"));
                    column_categoryName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
                    column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
                    column_color.setCellValueFactory(new PropertyValueFactory<>("color"));
                    column_price.setCellValueFactory(new PropertyValueFactory<>("price"));
                    column_purchase_date.setCellValueFactory(new PropertyValueFactory<>("purchase_date"));
                    column_manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
                    column_purchased_from.setCellValueFactory(new PropertyValueFactory<>("purchased_from"));
                    column_quality.setCellValueFactory(new PropertyValueFactory<>("quality"));
                    column_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
                    column_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));


                    tableView_articles.setItems(observableList);
                    button_showAll.setMouseTransparent(false);
                });

                return null;
            }

            ;
        };

        new Thread(showAllTask).start();
    }


    private void executeAddArticle() {
        Map<String, String> fields = new HashMap<>();

        if (datePicker_purchase_date.getValue() == null) {
            AlertService.showErrorAlert("Kaufdatum darf nicht leer sein.");
            return;
        }

        if (choiceBox_categories.getValue() == null) {
            AlertService.showErrorAlert("Kategorie darf nicht leer sein.");
            return;
        }

        fields.put("category_id", String.valueOf(choiceBox_categories.getValue().getID()));
        fields.put("name", field_name.getText());
        fields.put("color", field_color.getText());
        fields.put("price", field_price.getText());
        fields.put("purchase_date", datePicker_purchase_date.getValue().toString());
        fields.put("manufacturer", field_manufacturer.getText());
        fields.put("purchased_from", field_purchased_from.getText());
        fields.put("quality", field_quality.getText());
        fields.put("amount", field_amount.getText());
        fields.put("stock", field_stock.getText());

        if (!articleService.addArticle(fields)) {
            return;
        }

        executeShowAllArticles();
    }


    private void executeUpdateArticle() {
        Map<String, String> fields = new HashMap<>();

        Article selectedArticle = tableView_articles.getSelectionModel().getSelectedItem();

        if (selectedArticle == null) {
            AlertService.showErrorAlert("Bitte wählen Sie einen Artikel aus der Tabelle aus, um ihn zu bearbeiten.");
            return;
        }

        if (choiceBox_categories.getValue() != null) {
            fields.put("category_id", String.valueOf(choiceBox_categories.getValue().getID()));
        }

        fields.put("name", field_name.getText());
        fields.put("article_number", field_articleNumber.getText());
        fields.put("color", field_color.getText());
        fields.put("price", field_price.getText());
        if (datePicker_purchase_date.getValue() != null) {
            fields.put("purchase_date", datePicker_purchase_date.getValue().toString());
        }
        fields.put("manufacturer", field_manufacturer.getText());
        fields.put("purchased_from", field_purchased_from.getText());
        fields.put("quality", field_quality.getText());
        fields.put("amount", field_amount.getText());
        fields.put("stock", field_stock.getText());

        if (!articleService.updateArticle(fields, selectedArticle)) {
            return;
        }

        executeShowAllArticles();
    }


    private void executeShowAvalibleArticles() {
        ObservableList<Article> observableList = FXCollections.observableArrayList(articleService.getAvailableArticles());

        button_showAll.setMouseTransparent(true);

        Task<Void> showAllTask = new Task<>() {

            @Override
            protected Void call() {

                Platform.runLater(() -> {
                    clearFields();

                    column_articleNumber.setCellValueFactory(new PropertyValueFactory<>("articleNumber"));
                    column_categoryName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
                    column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
                    column_color.setCellValueFactory(new PropertyValueFactory<>("color"));
                    column_price.setCellValueFactory(new PropertyValueFactory<>("price"));
                    column_purchase_date.setCellValueFactory(new PropertyValueFactory<>("purchase_date"));
                    column_manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
                    column_purchased_from.setCellValueFactory(new PropertyValueFactory<>("purchased_from"));
                    column_quality.setCellValueFactory(new PropertyValueFactory<>("quality"));
                    column_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
                    column_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));


                    tableView_articles.setItems(observableList);
                    button_showAll.setMouseTransparent(false);
                });

                return null;
            }

            ;
        };

        new Thread(showAllTask).start();
    }


    private void executeDeleteArticle() {
        Article selectedArticle = tableView_articles.getSelectionModel().getSelectedItem();

        if (selectedArticle == null) {
            AlertService.showErrorAlert("Bitte wählen Sie einen Artikel aus der Tabelle aus, um ihn zu löschen.");
            return;
        }

        if (!articleService.deleteArticle(selectedArticle)) {
            return;
        }

        executeShowAllArticles();
    }


    private void executeSearchArticle() {
        Map<String, String> fields = new HashMap<>();


        if (choiceBox_categories.getValue() != null) {
            fields.put("category_id", String.valueOf(choiceBox_categories.getValue().getID()));
        }

        fields.put("name", field_name.getText());
        fields.put("article_number", field_articleNumber.getText());
        fields.put("color", field_color.getText());
        fields.put("price", field_price.getText());
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
            return;
        }

        button_search.setMouseTransparent(true);

        Task<Void> searchTask = new Task<>() {

            @Override
            protected Void call() {

                Platform.runLater(() -> {
                    ObservableList<Article> observableList = FXCollections.observableArrayList(listOfArticles);
                    tableView_articles.setItems(observableList);
                    button_search.setMouseTransparent(false);
                });

                return null;
            }
        };

        new Thread(searchTask).start();
    }


    private void executeExitArticleManagement() {
        articleService.clearSession();
        SceneManager.switchScene("/com/example/styleslick/Home-view.fxml", "Willkommen", false);
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
    private void onKeyPressedEnterShowAvailableArticles(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAvalibleArticles();
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
    private void onMouseClickedShowAvailableArticles(MouseEvent event) {
        executeShowAvalibleArticles();
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


    private void clearFields() {
        field_articleNumber.clear();
        field_name.clear();
        field_color.clear();
        field_price.clear();
        field_manufacturer.clear();
        field_purchased_from.clear();
        field_quality.clear();
        field_amount.clear();
        field_stock.clear();
        datePicker_purchase_date.setValue(null);
        choiceBox_categories.setValue(null);
    }
}
