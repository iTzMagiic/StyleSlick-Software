package com.example.styleslick.service;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Database;
import com.example.styleslick.rules.ArticleRules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ArticleService {

    private static final ArticleRules articleRules = new ArticleRules();
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

    public List<Article> getAvailableArticles() {
        return database.getAvailableArticles();
    }


    public boolean addArticle(Map<String, String> fields) {
        Map<String, String> filledFields = new HashMap<>();


        for (Map.Entry<String, String> field : fields.entrySet()) {
            if (field.getValue() == null || field.getValue().trim().isEmpty()) {
                continue;
            }
            filledFields.put(field.getKey(), field.getValue());
        }


        if (filledFields.containsKey("price")) {
            filledFields.replace("price", filledFields.get("price").replace(",", "."));
        }


        if (articleRules.isNotAllowedToAddArticle(filledFields)) {
            return false;
        }


        if (!database.addArticle(filledFields)) {
            AlertService.showErrorAlert("Artikel konnte nicht hinzugefügt werden.");
            return false;
        }

        AlertService.showConfirmAlert("Artikel wurde erfolgreich hinzugefügt!");
        return true;
    }


    public boolean updateArticle(Map<String, String> fields, Article article) {
        Map<String, String> filledFields = new HashMap<>();


        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }


        if (filledFields.containsKey("price")) {
            filledFields.replace("price", filledFields.get("price").replace(",", "."));
        }

        if (articleRules.isNotAllowedToUpdateArticles(filledFields)) {
            return false;
        }

        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich den Artikel mit der Artikel-Nr: '" + article.getArticleNumber() + "' bearbeiten?")) {
            AlertService.showErrorAlert("Der Artikel wird nicht bearbeitet.");
            return false;
        }

        if (filledFields.containsKey("article_number") && !filledFields.get("article_number").equals(article.getArticleNumber())) {
            if (database.isArticleNumberExist(filledFields.get("article_number"))) {
                AlertService.showErrorAlert("Die Artikel-Nr existiert bereits.");
                return false;
            }
        }

        if (!database.updateArticle(filledFields, article.getArticleID())) {
            AlertService.showErrorAlert("Fehler beim bearbeiten des Artikels.");
            return false;
        }

        AlertService.showConfirmAlert("Der Artikel wurde erfolgreich bearbeitet.");
        return true;
    }


    public List<Article> searchArticle(Map<String, String> fields) {
        Map<String, String> filledFields = new HashMap<>();
        List<Article> listOfArticles = new ArrayList<>();


        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                continue;
            }
            filledFields.put(entry.getKey(), entry.getValue());
        }


        if (filledFields.containsKey("price")) {
            filledFields.replace("price", filledFields.get("price").replace(",", "."));
        }

        if (articleRules.isNotAllowedToSearchArticle(filledFields)) {
            return listOfArticles;
        }

        listOfArticles = database.searchArticlesLike(filledFields);

        if (listOfArticles == null || listOfArticles.isEmpty()) {
            AlertService.showErrorAlert("Kein passender Artikel gefunden.");
            return listOfArticles;
        }

        return listOfArticles;
    }


    public boolean deleteArticle(Article article) {
        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich den Artikel mit der Artikel-Nr: '" + article.getArticleNumber() + "' löschen?")) {
            AlertService.showErrorAlert("Artikel wird nicht gelöscht.");
            return false;
        }

        if (database.hasArticleDependencies(article.getArticleID())) {
            AlertService.showErrorAlert("Bitte löschen Sie zuerst alle Bestellungen, die diesen Artikel enthalten, bevor Sie ihn entfernen.");
            return false;
        }

        if (!database.deleteArticle(article.getArticleID())) {
            AlertService.showErrorAlert("Artikel konnte nicht gelöscht werden.");
            return false;
        }

        AlertService.showConfirmAlert("Artikel erfolgreich gelöscht.");
        return true;
    }


    public void clearSession() {
        articleService = null;
        database = null;
    }
}
