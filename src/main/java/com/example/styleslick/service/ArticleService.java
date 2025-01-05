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

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie etwas ein.");
            return false;
        }

        // Prüfungen ob die Pflichtfelder ausgefüllt sind
        if (!filledFields.containsKey("name")) {
            RulesService.showErrorAlert("Bitte geben Sie ein Artikel Namen ein.");
            return false;
        }
        if (!filledFields.containsKey("farbe")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Farbe an.");
            return false;
        }
        if (!filledFields.containsKey("kaufpreis")) {
            RulesService.showErrorAlert("Bitte geben Sie ein Kaufpreis an.");
            return false;
        }

        filledFields.replace("kaufpreis", filledFields.get("kaufpreis").replace(",", "."));
        try {
            Double.parseDouble(filledFields.get("kaufpreis"));
        } catch (NumberFormatException e) {
            RulesService.showErrorAlert("Bitte ein Gültigen Kaufpreis eingeben.");
            return false;
        }

        if (!filledFields.containsKey("purchased_from")) {
            RulesService.showErrorAlert("Bitte geben Sie an, wo Sie es gekauft haben.");
            return false;
        }
        if (!filledFields.containsKey("menge")) {
            RulesService.showErrorAlert("Bitte geben Sie die Menge an.");
            return false;
        }
        if (!filledFields.get("menge").matches("\\d+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            return false;
        }
        if (filledFields.containsKey("bestand") && !filledFields.get("bestand").matches("[0-9]+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige Bestand an.");
            return false;
        }

        if (!database.addArticle(filledFields)) {
            RulesService.showErrorAlert("Fehler beim hinzufügen in die Datenbank.");
            return false;
        }

        RulesService.showConfirmAlert("Artikel wurde erfolgreich hinzugefügt!");
        return true;
    }


    public boolean updateArticle(Map<String, String> fields, int articleID) {
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie etwas an um den Artikel zu bearbeiten.");
            return false;
        }

        if (!RulesService.showConfirmAlertResult("Möchten Sie wirklich den Artikel mit der Artikel-Nr " + articleID + " bearbeiten?")){
            RulesService.showErrorAlert("Artikel wird nicht bearbeitet.");
            return false;
        }

        if (filledFields.containsKey("kaufpreis")) {
            filledFields.replace("kaufpreis", filledFields.get("kaufpreis").replace(",", "."));
            try {
                Double.parseDouble(filledFields.get("kaufpreis"));
            } catch (NumberFormatException e) {
                RulesService.showErrorAlert("Bitte ein Gültigen Kaufpreis eingeben.");
                return false;
            }
        }

        if (filledFields.containsKey("menge") && !filledFields.get("menge").matches("[0-9]+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            return false;
        }
        if (filledFields.containsKey("bestand") && !filledFields.get("bestand").matches("[0-9]+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige Bestand an.");
            return false;
        }

        if (!database.updateArticle(filledFields, articleID)) {
            RulesService.showErrorAlert("Fehler beim bearbeiten des Artikels.");
            return false;
        }

        RulesService.showConfirmAlert("Der Artikel wurde erfolgreich bearbeitet.");
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

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie etwas ein.");
            return listOfArticles;
        }

        if (filledFields.containsKey("kaufpreis")) {
            filledFields.replace("kaufpreis", filledFields.get("kaufpreis").replace(",", "."));
            try {
                Double.parseDouble(filledFields.get("kaufpreis"));
            } catch (NumberFormatException e) {
                RulesService.showErrorAlert("Bitte ein Gültigen Kaufpreis eingeben.");
                return listOfArticles;
            }
        }

        if (filledFields.containsKey("menge") && !filledFields.get("menge").matches("\\d+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            return listOfArticles;
        }
        if (filledFields.containsKey("bestand") && !filledFields.get("bestand").matches("[0-9]+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige Bestand an.");
            return listOfArticles;
        }


        listOfArticles = database.searchArticleLike(filledFields);


        if (listOfArticles == null || listOfArticles.isEmpty()) {
            RulesService.showErrorAlert("Es wurde kein passender Artikel gefunden.");
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
