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


//    public boolean addArticle(Map<String, String> fields) {
//        logger.debug("START Methode addArticle().");
//        Map<String, String> filledFields = new HashMap<>();
//
//        for (Map.Entry<String, String> field : fields.entrySet()) {
//            if (field.getValue() == null || field.getValue().trim().isEmpty()) {
//                continue;
//            }
//            filledFields.put(field.getKey(), field.getValue());
//        }
//
//        if (filledFields.isEmpty()) {
//            AlertService.showErrorAlert("Bitte geben Sie etwas ein.");
//            return false;
//        }
//
//        // Prüfungen ob die Pflichtfelder ausgefüllt sind
//        if (!filledFields.containsKey("name")) {
//            AlertService.showErrorAlert("Bitte geben Sie ein Artikel Namen ein.");
//            logger.warn("Benutzer hat kein Artikelnamen eingegeben ENDE.\n");
//            return false;
//        }
//        if (!filledFields.containsKey("color")) {
//            AlertService.showErrorAlert("Bitte geben Sie eine color an.");
//            logger.warn("Benutzer hat keine Artikelfarbe eingegeben ENDE.\n");
//            return false;
//        }
//        if (!filledFields.containsKey("purchase_price")) {
//            AlertService.showErrorAlert("Bitte geben Sie ein Kaufpreis an.");
//            logger.warn("Benutzer hat kein Kaufpreis eingegeben ENDE.\n");
//            return false;
//        }
//
//        filledFields.replace("purchase_price", filledFields.get("purchase_price").replace(",", "."));
//        try {
//            Double.parseDouble(filledFields.get("purchase_price"));
//        } catch (NumberFormatException e) {
//            AlertService.showErrorAlert("Bitte ein Gültigen purchase_price eingeben.");
//            logger.error("Benutzer hat kein Gültigen Kaufpreis eingegeben FEHLER: {} ENDE.\n", e.getMessage(), e);
//            return false;
//        }
//
//        if (!filledFields.containsKey("purchased_from")) {
//            AlertService.showErrorAlert("Bitte geben Sie an, wo Sie es gekauft haben.");
//            logger.warn("Keine Eingabe wo es gekauft wurde ENDE.\n");
//            return false;
//        }
//        if (!filledFields.containsKey("amount")) {
//            AlertService.showErrorAlert("Bitte geben Sie die Menge an.");
//            logger.warn("Es wurde keine Menge angegeben ENDE.\n");
//            return false;
//        }
//        if (!filledFields.get("amount").matches("\\d+")) {
//            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
//            logger.warn("Es wurde keine Gültige Mengen angegeben ENDE.\n");
//            return false;
//        }
//        if (filledFields.containsKey("stock") && !filledFields.get("stock").matches("[0-9]+")) {
//            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Bestand an.");
//            logger.warn("Es wurde kein gültiger Bestand angegeben ENDE.\n");
//            return false;
//        }
//
//        if (!database.addArticle(filledFields)) {
//            AlertService.showErrorAlert("Fehler beim hinzufügen in die Datenbank.");
//            logger.warn("Artikel wurde nicht in die Datenbank importiert ENDE.\n");
//            return false;
//        }
//
//        AlertService.showConfirmAlert("Artikel wurde erfolgreich hinzugefügt!");
//        logger.debug("Methode addArticle() erfolgreich END.\n");
//        return true;
//    }

    public boolean addArticle(Map<String, String> fields) {
        logger.debug("START Methode addArticle().");
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> field : fields.entrySet()) {
            if (field.getValue() == null || field.getValue().trim().isEmpty()) {
                continue;
            }
            filledFields.put(field.getKey(), field.getValue());
        }

        if (articleRules.isAllowedToAddArticle(filledFields)) {
            return false;
        }

        filledFields.replace("purchase_price", filledFields.get("purchase_price").replace(",", "."));


        if (!database.addArticle(filledFields)) {
            AlertService.showErrorAlert("Artikel konnte nicht hinzugefügt werden.");
            logger.warn("Artikel wurde nicht in die Datenbank importiert ENDE.\n");
            return false;
        }

        AlertService.showConfirmAlert("Artikel wurde erfolgreich hinzugefügt!");
        logger.debug("Methode addArticle() erfolgreich END.\n");
        return true;
    }


    public boolean updateArticle(Map<String, String> fields, int articleID) {
        logger.debug("Methode updateArticle() START.");
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie etwas an um den Artikel zu bearbeiten.");
            logger.warn("Liste leer ENDE.\n");
            return false;
        }

        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich den Artikel mit der Artikel-Nr " + articleID + " bearbeiten?")){
            AlertService.showErrorAlert("Artikel wird nicht bearbeitet.");
            logger.warn("Benutzer bricht Artikel bearbeitung ab ENDE.\n");
            return false;
        }

        if (filledFields.containsKey("purchase_price")) {
            filledFields.replace("purchase_price", filledFields.get("purchase_price").replace(",", "."));
            try {
                Double.parseDouble(filledFields.get("purchase_price"));
            } catch (NumberFormatException e) {
                AlertService.showErrorAlert("Bitte ein Gültigen purchase_price eingeben.");
                logger.error("Benutzer hat kein Gültigen Kaufpreis eingegeben FEHLER: {} ENDE.\n", e.getMessage(), e);
                return false;
            }
        }

        if (filledFields.containsKey("amount") && !filledFields.get("amount").matches("[0-9]+")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            logger.warn("Es wurde keine Gültige Mengen angegeben ENDE.\n");
            return false;
        }
        if (filledFields.containsKey("stock") && !filledFields.get("stock").matches("[0-9]+")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Bestand an.");
            logger.warn("Es wurde kein gültiger Bestand angegeben ENDE.\n");
            return false;
        }

        if (!database.updateArticle(filledFields, articleID)) {
            AlertService.showErrorAlert("Fehler beim bearbeiten des Artikels.");
            logger.warn("Artikel wurde nicht bearbeitet ENDE.\n");
            return false;
        }

        AlertService.showConfirmAlert("Der Artikel wurde erfolgreich bearbeitet.");
        logger.debug("Methode updateArticle() erfolgreich ENDE.\n");
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
        if (AlertService.showConfirmAlertResult("Möchten Sie wirklich den Artikel mit der Artikel Nummer '" + articleID + "' löschen?")) {
            return database.deleteArticle(articleID);
        }
        return false;
    }


    public void clearSession() {
        articleService = null;
        database = null;
    }
}
