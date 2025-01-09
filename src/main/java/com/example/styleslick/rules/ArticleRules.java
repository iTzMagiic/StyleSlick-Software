package com.example.styleslick.rules;

import com.example.styleslick.service.RulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ArticleRules {

    private static final Logger logger = LoggerFactory.getLogger(ArticleRules.class);


    public static boolean isValidPurchasePrice(Map<String, String> filledFields) {
        filledFields.replace("purchase_price", filledFields.get("purchase_price").replace(",", "."));
        try {
            Double.parseDouble(filledFields.get("purchase_price"));
        } catch (NumberFormatException e) {
            RulesService.showErrorAlert("Bitte ein Gültigen purchase_price eingeben.");
            logger.error("Benutzer hat kein Gültigen Kaufpreis eingegeben FEHLER: {} ENDE.\n", e.getMessage(), e);
            return false;
        }
        return true;
    }

}
