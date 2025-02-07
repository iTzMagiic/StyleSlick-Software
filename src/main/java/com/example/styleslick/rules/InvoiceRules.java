package com.example.styleslick.rules;

import com.example.styleslick.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class InvoiceRules {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceRules.class);


    public boolean isNotAllowedToAddInvoice(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie etwas ein.");
            return true;
        }

        if (!filledFields.containsKey("customer_id")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Kunden-Nr ein.");
            return true;
        }

        if (!filledFields.containsKey("payment_method")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Zahlungsmethode ein.");
            return true;
        }

        if (!filledFields.containsKey("payment_amount")) {
            AlertService.showErrorAlert("Bitte geben Sie ein Betrag ein.");
            return true;
        }

        if (isNotValidDouble(filledFields.get("payment_amount"))) {
            AlertService.showErrorAlert("Bitte geben Sie ein gültigen Betrag ein.");
            return true;
        }

        if (filledFields.containsKey("shipping_cost") && isNotValidDouble(filledFields.get("shipping_cost"))) {
            AlertService.showErrorAlert("Bitte geben Sie ein gültigen Versandpreis an.");
            return true;
        }


        return false;
    }


    public boolean isNotAllowedToAddItemToInvoice(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Geben Sie bitte Artikel-Nr und die Menge an.");
            return true;
        }

        if (!filledFields.containsKey("article_number")) {
            AlertService.showErrorAlert("Bitte geben Sie die Artikel-Nr ein.");
            return true;
        }

        //TODO:: Methode isNotValidArticleNumber muss noch erstellt werden
//        if (isNotValidArticleNumber(filledFields.get("article_number"))) {
//            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Artikel-Nr an.");
//            return true;
//        }

        if (!filledFields.containsKey("amount")) {
            AlertService.showErrorAlert("Bitte geben Sie die Menge der Bestellten Artikel an.");
            return true;
        }

        if (isNotValidAmount(filledFields.get("amount"))) {
            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            return true;
        }


        return false;
    }



    private boolean isNotValidAmount(String input) {
        if (!input.matches("[1-9]\\d*")) {
            return true;
        }
        return false;
    }


    private boolean isNotValidCustomerNumber(Map<String, String> filledFields) {
        return false;
    }


    public boolean isNotValidDouble(String input) {
        try {
            double inputDouble = Double.parseDouble(input);

            if (inputDouble < 0) {
                return true;
            }

        } catch (NumberFormatException e) {
            logger.error("ERROR isNotValidDouble() fehlgeschlagen Benutzer hat kein gültigen Wert angegeben der zum Double konvertiert werden konnte" +
                    " angegeben. FEHLER: {} ", e.getMessage(), e);
            return true;
        }

        return false;
    }

}
