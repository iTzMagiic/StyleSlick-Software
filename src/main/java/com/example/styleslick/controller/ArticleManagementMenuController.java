package com.example.styleslick.controller;

import com.example.styleslick.model.Article;
import com.example.styleslick.service.ArticleService;
import com.example.styleslick.service.CategoryService;
import com.example.styleslick.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class ArticleManagementMenuController implements Initializable {

    private ArticleService articleService;
    private CategoryService categoryService;

    @FXML
    private ChoiceBox<?> choice_category_id;
    @FXML
    private TableView<Article> tableView_articles;
    @FXML
    private TableColumn<Article, Integer> column_article_id;
    @FXML
    private TableColumn<Article, String> column_category_id;
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
    private TextField field_name;
    @FXML
    private TextField field_farbe;
    @FXML
    private TextField field_kaufpreis;
    @FXML
    private TextField field_kaufdatum;
    @FXML
    private TextField field_hersteller;
    @FXML
    private TextField field_gekauft_ueber;
    @FXML
    private TextField field_verarbeitung;
    @FXML
    private TextField field_menge;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        articleService = ArticleService.getInstance();
        categoryService = CategoryService.getInstance();
    }


    private void executeShowAllArticles() {
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

        fields.put("name", field_name.getText());
        fields.put("farbe", field_farbe.getText());
        fields.put("kaufpreis", field_kaufpreis.getText() );
        fields.put("kaufdatum", field_kaufdatum.getText());
        fields.put("hersteller", field_hersteller.getText());
        fields.put("gekauft_ueber", field_gekauft_ueber.getText());
        fields.put("verarbeitung", field_verarbeitung.getText());
        fields.put("menge", field_menge.getText());

        if (articleService.addArticle(fields)) {
            field_name.clear();
            field_farbe.clear();
            field_kaufpreis.clear();
            field_kaufdatum.clear();
            field_hersteller.clear();
            field_gekauft_ueber.clear();
            field_verarbeitung.clear();
            field_menge.clear();
        }
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
    void onKeyPressedDeleteArticle(KeyEvent event) {

    }


    @FXML
    void onKeyPressedEnterAddArticle(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeAddArticle();
        }
    }


    @FXML
    void onKeyPressedEnterSearchArticle(KeyEvent event) {

    }


    @FXML
    void onMouseCLickedDeleteArticle(MouseEvent event) {

    }


    @FXML
    void onMouseClickedAddArticle(MouseEvent event) {
        executeAddArticle();
    }


    @FXML
    void onMouseClickedSearchArticle(MouseEvent event) {

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
