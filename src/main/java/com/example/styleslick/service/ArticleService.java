package com.example.styleslick.service;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Database;
import com.example.styleslick.rules.ArticleRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    TODO:: Redundante Methoden auslagen, in eine seperate ArticleRules Klasse!!!!
 */

public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);
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


    public boolean addArticle(Map<String, String> fields) {
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> field : fields.entrySet()) {
            if (field.getValue() == null || field.getValue().trim().isEmpty()) {
                continue;
            }
            filledFields.put(field.getKey(), field.getValue());
        }

        if (!articleRules.isAllowedToAddArticle(filledFields)) {
            return false;
        }

        filledFields.replace("purchase_price", filledFields.get("purchase_price").replace(",", "."));


        if (!database.addArticle(filledFields)) {
            AlertService.showErrorAlert("Artikel konnte nicht hinzugefügt werden.");
            return false;
        }

        AlertService.showConfirmAlert("Artikel wurde erfolgreich hinzugefügt!");
        return true;
    }


    public boolean updateArticle(Map<String, String> fields, int articleID) {
        logger.debug("\n\nSTART updateArticle().");
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        if (!articleRules.isAllowedToUpdateArticle(filledFields)) {
            return false;
        }

        if (filledFields.containsKey("purchase_price")) {
            filledFields.replace("purchase_price", filledFields.get("purchase_price").replace(",", "."));
        }

        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich den Artikel mit der Artikel-Nr " + articleID + " bearbeiten?")) {
            AlertService.showErrorAlert("Der Artikel wird nicht bearbeitet.");
            return false;
        }

        if (!database.updateArticle(filledFields, articleID)) {
            AlertService.showErrorAlert("Fehler beim bearbeiten des Artikels.");
            return false;
        }

        AlertService.showConfirmAlert("Der Artikel wurde erfolgreich bearbeitet.");
        return true;
    }


    public List<Article> searchArticle(Map<String, String> fields) {
        logger.debug("Methode searchArticle() START.");
        Map<String, String> filledFields = new HashMap<>();
        List<Article> listOfArticles = new ArrayList<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                continue;
            }
            filledFields.put(entry.getKey(), entry.getValue());
        }

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie etwas ein.");
            logger.warn("Liste leer ENDE.\n");
            return listOfArticles;
        }

        if (filledFields.containsKey("purchase_price")) {
            filledFields.replace("purchase_price", filledFields.get("purchase_price").replace(",", "."));
            try {
                Double.parseDouble(filledFields.get("purchase_price"));
            } catch (NumberFormatException e) {
                AlertService.showErrorAlert("Bitte ein Gültigen Kaufpreis eingeben.");
                logger.error("Benutzer hat kein Gültigen Kaufpreis eingegeben FEHLER: {} ENDE.\n", e.getMessage(), e);
                return listOfArticles;
            }
        }

        if (filledFields.containsKey("amount") && !filledFields.get("amount").matches("\\d+")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            logger.warn("Es wurde keine Gültige Mengen angegeben ENDE.\n");
            return listOfArticles;
        }
        if (filledFields.containsKey("stock") && !filledFields.get("stock").matches("[0-9]+")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Bestand an.");
            logger.warn("Es wurde kein gültiger Bestand angegeben ENDE.\n");
            return listOfArticles;
        }


        listOfArticles = database.searchArticleLike(filledFields);


        if (listOfArticles == null || listOfArticles.isEmpty()) {
            AlertService.showErrorAlert("Es wurde kein passender Artikel gefunden.");
            logger.warn("Es wurde kein passender Artikel gefunden ENDE.\n");
            return listOfArticles;
        }

        logger.debug("Methode searchArticle() erfolgreich END.\n");
        return listOfArticles;
    }


    public boolean deleteArticle(int articleID) {
        if (AlertService.showConfirmAlertResult("Möchten Sie wirklich den Artikel mit der Artikel-Nr '" + articleID + "' löschen?")) {
            return database.deleteArticle(articleID);
        }
        return false;
    }


    public void clearSession() {
        articleService = null;
        database = null;
    }
}
