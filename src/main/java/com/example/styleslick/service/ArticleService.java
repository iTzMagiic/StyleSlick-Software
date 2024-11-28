package com.example.styleslick.service;

import com.example.styleslick.model.Database;

import java.util.Map;

public class ArticleService {

    private static ArticleService articleService;
    private Database database;

    private ArticleService() {}

    public static ArticleService getInstance() {
        if (articleService == null) {
            articleService = new ArticleService();
        }
        return articleService;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void addArticle(Map<String, String> fields) {
        //TODO:: addArticle Methode erstellen
    }

}
