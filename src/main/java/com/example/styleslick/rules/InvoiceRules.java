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

        if (isNotValidPrice(filledFields)) {
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


    private boolean isNotValidCustomerNumber(Map<String, String> filledFields) {
        return false;
    }

    private boolean isNotValidPrice(Map<String, String> filledFields) {

        if (filledFields.containsKey("shipping_cost")) {
            try {
                Double.parseDouble(filledFields.get("shipping_cost"));
            } catch (NumberFormatException e) {
                AlertService.showErrorAlert("Bitte geben Sie ein gültigen Versandpreis an.");
                logger.error("ERROR isNotValidPrice() fehlgeschlagen Benutzer hat kein gültigen Versandpreis angegeben. FEHLER: {} ", e.getMessage(), e);
                return true;
            }
        }

        if (filledFields.containsKey("payment_amount")) {
            try {
                Double.parseDouble(filledFields.get("payment_amount"));
            } catch (NumberFormatException e) {
                AlertService.showErrorAlert("Bitte geben Sie ein gültigen Betrag ein.");
                logger.error("ERROR isNotValidPrice() fehlgeschlagen Benutzer hat kein gültigen Betrag angegeben. FEHLER: {} ", e.getMessage(), e);
                return true;
            }
        }

        return false;
    }

}
