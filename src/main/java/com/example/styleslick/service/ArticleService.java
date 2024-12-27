package com.example.styleslick.service;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Database;

import java.util.HashMap;
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
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> field : fields.entrySet()) {
            if (field.getValue() == null || field.getValue().isEmpty()) {continue;}
            filledFields.put(field.getKey(), field.getValue());
        }
        //TODO:: paar Prüfungen machen was den wirklich eingegeben wurden ist in filledFields.value();

//        if (!database.addArticle(filledFields)) {
//            RulesService.showErrorAlert("Fehler beim hinzufügen in die Datenbank.");
//            return false;
//        }

        RulesService.showConfirmAlert("Artikel wurde erfolgreich hinzugefügt!");
        return true;
    }


    public void clearSession() {
        articleService = null;
        database = null;
    }

}
