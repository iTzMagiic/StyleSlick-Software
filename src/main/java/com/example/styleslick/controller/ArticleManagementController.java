package com.example.styleslick.controller;

import com.example.styleslick.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ArticleManagementController {


    @FXML
    private void executeAddArticle() {
        SceneManager.switchScene("/com/example/styleslick/articleManagement/addArticle-view.fxml", "Artikel hinzufügen");
    }
    @FXML
    private void onKeyPressedEnterAddArticle(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeAddArticle();
        }
    }
    @FXML
    private void onMouseClickedAddArticle(MouseEvent event) {
        executeAddArticle();
    }



    @FXML
    private void executeShowArticles() {
        SceneManager.switchScene("/com/example/styleslick/articleManagement/showArticles-view.fxml", "Artikel anzeigen");
    }
    @FXML
    private void onKeyPressedShowArticles(KeyEvent event){
        if(event.getCode().toString().equals("ENTER")) {
            executeShowArticles();
        }
    }
    @FXML
    private void onMouseClickedShowArticles(MouseEvent event) {
        executeShowArticles();
    }



    @FXML
    private void executeUpdateArticle() {
        SceneManager.switchScene("/com/example/styleslick/articleManagement/updateArticle-view.fxml", "Artikel bearbeiten");
    }
    @FXML
    private void onKeyPressedUpdateArticle(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")) {
            executeUpdateArticle();
        }
    }
    @FXML
    private void onMouseClickedUpdateArticle(MouseEvent event) {
        executeUpdateArticle();
    }



    @FXML
    private void executeExitArticleManagement(){
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
    }
    @FXML
    private void onKeyPressedEnterExitArticleManagement(KeyEvent event){
        if(event.getCode().toString().equals("ENTER")){
            executeExitArticleManagement();
        }
    }
    @FXML
    private void onMouseClickedExitArticleManagement(MouseEvent event){
        executeExitArticleManagement();
    }

}
