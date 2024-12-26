package com.example.styleslick.controller;

import com.example.styleslick.model.Article;
import com.example.styleslick.service.ArticleService;
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
import java.util.ResourceBundle;

public class ArticleManagementMenuControllerNEW implements Initializable {

    private ArticleService articleService;


    @FXML
    private ChoiceBox<?> choice_category_id;
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
    private TextField field_name;
    @FXML
    private TextField field_color;
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
    }

    @FXML
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

    }

    @FXML
    void onKeyPressedEnterExitArticleManagement(KeyEvent event) {

    }

    @FXML
    void onKeyPressedEnterSearchArticle(KeyEvent event) {

    }


    @FXML
    void onMouseCLickedDeleteArticle(MouseEvent event) {

    }

    @FXML
    void onMouseClickedAddArticle(MouseEvent event) {

    }

    @FXML
    void onMouseClickedExitArticleManagement(MouseEvent event) {

    }

    @FXML
    void onMouseClickedSearchArticle(MouseEvent event) {

    }



}
