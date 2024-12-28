package com.example.styleslick.service;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Database;

import java.util.ArrayList;
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
            if (field.getValue() == null || field.getValue().trim().isEmpty()) {
                continue;
            }
            filledFields.put(field.getKey(), field.getValue());
        }

        // Prüfungen ob die Pflichtfelder ausgefüllt sind
        if (!filledFields.containsKey("name") || filledFields.get("name") == null || filledFields.get("name").trim().isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie ein Artikel Namen ein.");
            return false;
        }
        if (!filledFields.containsKey("farbe") || filledFields.get("farbe") == null || filledFields.get("farbe").trim().isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie eine Farbe an.");
            return false;
        }
        if (!filledFields.containsKey("kaufpreis") || filledFields.get("kaufpreis") == null || filledFields.get("kaufpreis").trim().isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie ein Kaufpreis an.");
            return false;
        }

        filledFields.replace("kaufpreis", filledFields.get("kaufpreis").trim().replace(",", "."));
        try {
            Double.parseDouble(filledFields.get("kaufpreis"));
        } catch (NumberFormatException e) {
            RulesService.showErrorAlert("Bitte ein Gültigen Kaufpreis eingeben.");
            return false;
        }

        if (!filledFields.containsKey("gekauft_ueber") || filledFields.get("gekauft_ueber") == null || filledFields.get("gekauft_ueber").trim().isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie an, wo Sie es gekauft haben.");
            return false;
        }
        if (!filledFields.containsKey("menge") || filledFields.get("menge") == null || filledFields.get("menge").trim().isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie die Menge an.");
            return false;
        }

        try {
            Integer.parseInt(filledFields.get("menge"));
        } catch (NumberFormatException e) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            return false;
        }
        if (!database.addArticle(filledFields)) {
            RulesService.showErrorAlert("Fehler beim hinzufügen in die Datenbank.");
            return false;
        }

        RulesService.showConfirmAlert("Artikel wurde erfolgreich hinzugefügt!");
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

        listOfArticles = database.searchArticleLike(fields);

        if (listOfArticles == null || listOfArticles.isEmpty()) {
            RulesService.showErrorAlert("Es wurde kein Artikel gefunden.");
            return listOfArticles;
        }

        return listOfArticles;
    }


    public boolean deleteArticle(int articleID) {
        if (RulesService.showConfirmAlertResult("Möchten Sie wirklich den Artikel mit der Artikel Nummer '" + articleID + "' löschen?")) {
            return database.deleteArticle(articleID);
        }
        return false;
    }


    public void clearSession() {
        articleService = null;
        database = null;
    }
}
