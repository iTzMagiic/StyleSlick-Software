package com.example.styleslick.rules;

import com.example.styleslick.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ArticleRules {

    private static final Logger logger = LoggerFactory.getLogger(ArticleRules.class);

    public boolean isAllowedToAddArticle(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie etwas ein.");
            return false;
        }

        if (!filledFields.containsKey("name")) {
            AlertService.showErrorAlert("Bitte geben Sie ein Artikelname ein.");
            return false;
        }

        if (!filledFields.containsKey("color")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Farbe ein.");
            return false;
        }

        if (!filledFields.containsKey("purchase_price")) {
            AlertService.showErrorAlert("Bitte geben Sie den Kaufpreis ein.");
            return false;
        }

        if (!isValidPurchasePrice(filledFields)) {
            return false;
        }

        if (!filledFields.containsKey("purchased_from")) {
            AlertService.showErrorAlert("Bitte geben Sie an, wo Sie es gekauft haben.");
            return false;
        }

        if (!filledFields.containsKey("amount")) {
            AlertService.showErrorAlert("Bitte geben Sie die Menge an.");
            return false;
        }

        if (!isValidAmount(filledFields)) {
            return false;
        }

        if (filledFields.containsKey("stock") && !isValidStock(filledFields)) {
            return false;
        }

        return true;
    }

    public boolean isAllowedToUpdateArticle(Map<String, String> filledFields) {
        // TODO:: Methode ausarbeiten!
        return true;
    }

    private boolean isValidPurchasePrice(Map<String, String> filledFields) {
        try {
            Double.parseDouble(filledFields.get("purchase_price"));
        } catch (NumberFormatException e) {
            AlertService.showErrorAlert("Bitte geben Sie ein Gültigen Kaufpreis ein.");
            logger.error("Benutzer hat kein Gültigen Kaufpreis eingegeben FEHLER: {} ENDE.\n", e.getMessage(), e);
            return false;
        }
        return true;
    }

    private boolean isValidAmount(Map<String, String> filledFields) {
        if (!filledFields.get("amount").matches("\\d+")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            return false;
        }
        return true;
    }

    private boolean isValidStock(Map<String, String> filledFields) {
        if (!filledFields.get("stock").matches("[0-9]+")) {
            AlertService.showErrorAlert("Bitte geben Sie ein Gültigen Bestand an.");
            return false;
        }
        return true;
    }

}
