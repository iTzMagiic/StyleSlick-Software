package com.example.styleslick.rules;

import com.example.styleslick.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ArticleRules {

    private static final Logger logger = LoggerFactory.getLogger(ArticleRules.class);


    public boolean isNotAllowedToAddArticle(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie die Pflichtfelder ein.");
            return true;
        }

        if (!filledFields.containsKey("name")) {
            AlertService.showErrorAlert("Bitte geben Sie ein Artikelname ein.");
            return true;
        }

        if (!filledFields.containsKey("color")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Farbe ein.");
            return true;
        }

        if (!filledFields.containsKey("price")) {
            AlertService.showErrorAlert("Bitte geben Sie den Kaufpreis ein.");
            return true;
        }

        if (isNotValidPurchasePrice(filledFields)) {
            return true;
        }

        if (!filledFields.containsKey("purchased_from")) {
            AlertService.showErrorAlert("Bitte geben Sie an, wo Sie es gekauft haben.");
            return true;
        }

        if (!filledFields.containsKey("amount")) {
            AlertService.showErrorAlert("Bitte geben Sie die Menge an.");
            return true;
        }

        if (isNotValidAmount(filledFields)) {
            return true;
        }

        if (filledFields.containsKey("stock") && isNotValidStock(filledFields)) {
            return true;
        }

        return false;
    }


    public boolean isNotAllowedToUpdateArticles(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie etwas an um den Artikel zu bearbeiten.");
            return true;
        }

        if (filledFields.containsKey("price") && isNotValidPurchasePrice(filledFields)) {
            return true;
        }

        if (filledFields.containsKey("amount") && isNotValidAmount(filledFields)) {
            return true;
        }

        if (filledFields.containsKey("article_number") && isNotValidArticleNumber(filledFields.get("article_number"))) {
            return true;
        }

        if (filledFields.containsKey("stock") && isNotValidStock(filledFields)) {
            return true;
        }

        return false;
    }

    public boolean isNotAllowedToSearchArticle(Map<String, String> filledFields) {
        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie etwas an um den Artikel zu suchen.");
            return true;
        }

        if (filledFields.containsKey("price") && isNotValidPurchasePrice(filledFields)) {
            return true;
        }

        if (filledFields.containsKey("amount") && isNotValidAmount(filledFields)) {
            return true;
        }

        if (filledFields.containsKey("stock") && isNotValidStock(filledFields)) {
            return true;
        }

        return false;
    }


    private boolean isNotValidPurchasePrice(Map<String, String> filledFields) {
        try {
            Double.parseDouble(filledFields.get("price"));
        } catch (NumberFormatException e) {
            AlertService.showErrorAlert("Bitte geben Sie ein Gültigen Kaufpreis ein.");
            logger.error("ERROR isNotValidPurchasePrice() Benutzer hat kein Gültigen Kaufpreis eingegeben FEHLER: {} ENDE.\n", e.getMessage(), e);
            return true;
        }
        return false;
    }


    private boolean isNotValidAmount(Map<String, String> filledFields) {
        if (!filledFields.get("amount").matches("\\d+")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            return true;
        }
        return false;
    }


    private boolean isNotValidStock(Map<String, String> filledFields) {
        if (!filledFields.get("stock").matches("[0-9]+")) {
            AlertService.showErrorAlert("Bitte geben Sie ein Gültigen Bestand an.");
            return true;
        }
        return false;
    }


    private boolean isNotValidArticleNumber(String articleNumber) {
        if (!articleNumber.matches("^A\\d{8}$")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Artikel-Nr in dem Format an. A20250001");
            return true;
        }
        return false;
    }

}
