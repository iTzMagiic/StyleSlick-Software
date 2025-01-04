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
    private TableColumn<Article, String> column_farbe;
    @FXML
    private TableColumn<Article, String> column_kaufpreis;
    @FXML
    private TableColumn<Article, LocalDate> column_kaufdatum;
    @FXML
    private TableColumn<Article, String> column_hersteller;
    @FXML
    private TableColumn<Article, String> column_gekauft_ueber;
    @FXML
    private TableColumn<Article, String> column_verarbeitung;
    @FXML
    private TableColumn<Article, Integer> column_menge;
    @FXML
    private TableColumn<Article, Integer> column_bestand;
    @FXML
    private ChoiceBox<Category> choiceBox_category_id;
    @FXML
    private TextField field_name;
    @FXML
    private TextField field_farbe;
    @FXML
    private TextField field_kaufpreis;
    @FXML
    private DatePicker datePicker_kaufdatum;
    @FXML
    private TextField field_hersteller;
    @FXML
    private TextField field_gekauft_ueber;
    @FXML
    private TextField field_verarbeitung;
    @FXML
    private TextField field_menge;
    @FXML
    private TextField field_bestand;


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
        column_farbe.setCellValueFactory(new PropertyValueFactory<>("farbe"));
        column_kaufpreis.setCellValueFactory(new PropertyValueFactory<>("kaufpreis"));
        column_kaufdatum.setCellValueFactory(new PropertyValueFactory<>("kaufdatum"));
        column_hersteller.setCellValueFactory(new PropertyValueFactory<>("hersteller"));
        column_gekauft_ueber.setCellValueFactory(new PropertyValueFactory<>("gekauft_ueber"));
        column_verarbeitung.setCellValueFactory(new PropertyValueFactory<>("verarbeitung"));
        column_menge.setCellValueFactory(new PropertyValueFactory<>("menge"));
        column_bestand.setCellValueFactory(new PropertyValueFactory<>("bestand"));

        ObservableList<Article> observableList = FXCollections.observableArrayList(articleService.getAllArticles());
        tableView_articles.setItems(observableList);
    }


    private void executeAddArticle() {
        Map<String, String> fields = new HashMap<>();

        if (datePicker_kaufdatum.getValue() == null) {
            RulesService.showErrorAlert("Kaufdatum darf nicht Leer sein.");
            return;
        }

        if (choiceBox_category_id.getValue() == null) {
            RulesService.showErrorAlert("Kategorie darf nicht Leer sein.");
            return;
        }

        fields.put("category_id", String.valueOf(choiceBox_category_id.getValue().getID()));
        fields.put("name", field_name.getText());
        fields.put("farbe", field_farbe.getText());
        fields.put("kaufpreis", field_kaufpreis.getText());
        fields.put("kaufdatum", datePicker_kaufdatum.getValue().toString());
        fields.put("hersteller", field_hersteller.getText());
        fields.put("gekauft_ueber", field_gekauft_ueber.getText());
        fields.put("verarbeitung", field_verarbeitung.getText());
        fields.put("menge", field_menge.getText());
        fields.put("bestand", field_bestand.getText());

        if (articleService.addArticle(fields)) {
            field_name.clear();
            field_farbe.clear();
            field_kaufpreis.clear();
            field_hersteller.clear();
            field_gekauft_ueber.clear();
            field_verarbeitung.clear();
            field_menge.clear();
            field_bestand.clear();
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
        fields.put("farbe", field_farbe.getText());
        fields.put("kaufpreis", field_kaufpreis.getText());
        if (datePicker_kaufdatum.getValue() != null) {
            fields.put("kaufdatum", datePicker_kaufdatum.getValue().toString());
        }
        fields.put("hersteller", field_hersteller.getText());
        fields.put("gekauft_ueber", field_gekauft_ueber.getText());
        fields.put("verarbeitung", field_verarbeitung.getText());
        fields.put("menge", field_menge.getText());
        fields.put("bestand", field_bestand.getText());

        if (articleService.updateArticle(fields, selectedArticle.getArticle_id())) {
            choiceBox_category_id.setValue(null);
            field_name.clear();
            field_farbe.clear();
            field_kaufpreis.clear();
            field_hersteller.clear();
            field_gekauft_ueber.clear();
            field_verarbeitung.clear();
            field_menge.clear();
            field_bestand.clear();
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
        fields.put("farbe", field_farbe.getText());
        fields.put("kaufpreis", field_kaufpreis.getText());
        if (datePicker_kaufdatum.getValue() != null) {
            fields.put("kaufdatum", datePicker_kaufdatum.getValue().toString());
        }
        fields.put("hersteller", field_hersteller.getText());
        fields.put("gekauft_ueber", field_gekauft_ueber.getText());
        fields.put("verarbeitung", field_verarbeitung.getText());
        fields.put("menge", field_menge.getText());
        fields.put("bestand", field_bestand.getText());

        List<Article> listOfArticles = articleService.searchArticle(fields);

        if (listOfArticles == null || listOfArticles.isEmpty()) {
            choiceBox_category_id.setValue(null);
            field_name.clear();
            field_farbe.clear();
            field_kaufpreis.clear();
            field_hersteller.clear();
            field_gekauft_ueber.clear();
            field_verarbeitung.clear();
            field_menge.clear();
            field_bestand.clear();
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
