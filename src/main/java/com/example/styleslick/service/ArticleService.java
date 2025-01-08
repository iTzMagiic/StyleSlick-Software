package com.example.styleslick.service;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);
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
        logger.info("Methode addArticle() START.");
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> field : fields.entrySet()) {
            if (field.getValue() == null || field.getValue().trim().isEmpty()) {
                continue;
            }
            filledFields.put(field.getKey(), field.getValue());
        }

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie etwas ein.");
            logger.warn("Liste leer ENDE.\n");
            return false;
        }

        // Prüfungen ob die Pflichtfelder ausgefüllt sind
        if (!filledFields.containsKey("name")) {
            RulesService.showErrorAlert("Bitte geben Sie ein Artikel Namen ein.");
            logger.warn("Benutzer hat kein Artikelnamen eingegeben ENDE.\n");
            return false;
        }
        if (!filledFields.containsKey("color")) {
            RulesService.showErrorAlert("Bitte geben Sie eine color an.");
            logger.warn("Benutzer hat keine Artikelfarbe eingegeben ENDE.\n");
            return false;
        }
        if (!filledFields.containsKey("purchase_price")) {
            RulesService.showErrorAlert("Bitte geben Sie ein Kaufpreis an.");
            logger.warn("Benutzer hat kein Kaufpreis eingegeben ENDE.\n");
            return false;
        }

        filledFields.replace("purchase_price", filledFields.get("purchase_price").replace(",", "."));
        try {
            Double.parseDouble(filledFields.get("purchase_price"));
        } catch (NumberFormatException e) {
            RulesService.showErrorAlert("Bitte ein Gültigen purchase_price eingeben.");
            logger.error("Benutzer hat keine Gültige Zahl eingegeben FEHLER: {} ENDE.\n", e.getMessage(), e);
            return false;
        }

        if (!filledFields.containsKey("purchased_from")) {
            RulesService.showErrorAlert("Bitte geben Sie an, wo Sie es gekauft haben.");
            logger.warn("Keine Eingabe wo es gekauft wurde ENDE.\n");
            return false;
        }
        if (!filledFields.containsKey("amount")) {
            RulesService.showErrorAlert("Bitte geben Sie die Menge an.");
            logger.warn("Es wurde keine Menge angegeben ENDE.\n");
            return false;
        }
        if (!filledFields.get("amount").matches("\\d+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            return false;
        }
        if (filledFields.containsKey("stock") && !filledFields.get("stock").matches("[0-9]+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige stock an.");
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

        if (filledFields.containsKey("purchase_price")) {
            filledFields.replace("purchase_price", filledFields.get("purchase_price").replace(",", "."));
            try {
                Double.parseDouble(filledFields.get("purchase_price"));
            } catch (NumberFormatException e) {
                RulesService.showErrorAlert("Bitte ein Gültigen purchase_price eingeben.");
                return false;
            }
        }

        if (filledFields.containsKey("amount") && !filledFields.get("amount").matches("[0-9]+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige amount an.");
            return false;
        }
        if (filledFields.containsKey("stock") && !filledFields.get("stock").matches("[0-9]+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige stock an.");
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

        if (filledFields.containsKey("purchase_price")) {
            filledFields.replace("purchase_price", filledFields.get("purchase_price").replace(",", "."));
            try {
                Double.parseDouble(filledFields.get("purchase_price"));
            } catch (NumberFormatException e) {
                RulesService.showErrorAlert("Bitte ein Gültigen purchase_price eingeben.");
                return listOfArticles;
            }
        }

        if (filledFields.containsKey("amount") && !filledFields.get("amount").matches("\\d+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige amount an.");
            return listOfArticles;
        }
        if (filledFields.containsKey("stock") && !filledFields.get("stock").matches("[0-9]+")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Gültige stock an.");
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
