package com.example.styleslick.rules;

import com.example.styleslick.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ArticleRules {

    private static final Logger logger = LoggerFactory.getLogger(ArticleRules.class);


    public static boolean isValidPurchasePrice(Map<String, String> filledFields) {
        try {
            Double.parseDouble(filledFields.get("purchase_price"));
        } catch (NumberFormatException e) {
            AlertService.showErrorAlert("Bitte ein Gültigen purchase_price eingeben.");
            logger.error("Benutzer hat kein Gültigen Kaufpreis eingegeben FEHLER: {} ENDE.\n", e.getMessage(), e);
            return false;
        }
        return true;
    }

    public static boolean isValidAmount(Map<String, String> filledFields) {
        return filledFields.containsKey("amount") && filledFields.get("amount").matches("\\d+");
    }

    public static boolean isValidStock(Map<String, String> filledFields) {
        return filledFields.containsKey("stock") && filledFields.get("stock").matches("[0-9]+");
    }

}
