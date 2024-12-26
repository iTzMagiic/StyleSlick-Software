package com.example.styleslick.service;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Database;
import java.util.List;
import java.util.Map;

public class ArticleService {

    private static ArticleService articleService;
    private Database database;



    private ArticleService() {
    }

    public static synchronized ArticleService getInstance() {
        if (articleService == null) {
            articleService = new ArticleService();
        }
        return articleService;
    }


    public void setDatabase(Database database) {
        this.database = database;
    }


    public List<Article> getAllArticles() {
        return database.getAllArticles();
    }


    public boolean addArticle(Map<String, String> fields) {
        //TODO:: addArticle Methode erstellen
        return true;
    }


    public void clearSession() {
        articleService = null;
        database = null;
    }

}
