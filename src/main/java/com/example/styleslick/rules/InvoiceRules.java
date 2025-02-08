package com.example.styleslick.rules;

import com.example.styleslick.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class InvoiceRules {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceRules.class);


    public boolean isNotAllowedToAddInvoice(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte füllen Sie die Pflichtfelder aus um eine Bestellung hinzuzufügen.");
            return true;
        }

        //TODO:: customer_id muss noch zu customer_number umgeändert werden in InvoiceManagementController und neue Methoden in der Database klasse
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

        if (isNotValidArticleNumber(filledFields.get("article_number"))) {
            return true;
        }

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


    public boolean isNotAllowedToUpdateInvoice(Map<String, String> filledFields) {

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie was an um die Bestellung zu bearbeiten.");
            return true;
        }

        if (filledFields.containsKey("invoice_number") && isNotValidInvoiceNumber(filledFields.get("invoice_number"))) {
            return true;
        }

        if (filledFields.containsKey("amount") && isNotValidAmount(filledFields.get("amount"))) {
            AlertService.showErrorAlert("Bitte geben Sie eine Gültige Menge an.");
            return true;
        }

        if (filledFields.containsKey("customer_number") && isNotValidCustomerNumber(filledFields.get("customer_number"))) {
            AlertService.showErrorAlert("Bitte geben Sie eine Kunden-Nr ein.");
            return true;
        }

        if (filledFields.containsKey("payment_amount") && isNotValidDouble(filledFields.get("payment_amount"))) {
            AlertService.showErrorAlert("Bitte geben Sie ein gültigen Betrag ein.");
            return true;
        }

        if (filledFields.containsKey("shipping_cost") && isNotValidDouble(filledFields.get("shipping_cost"))) {
            AlertService.showErrorAlert("Bitte geben Sie ein gültigen Versandpreis an.");
            return true;
        }

        return false;
    }



    public boolean isNotValidAmount(String input) {
        return !input.matches("[1-9]\\d*");
    }


    private boolean isNotValidInvoiceNumber(String invoiceNumber) {
        if (!invoiceNumber.matches("^I\\d{8}$")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Bestell-Nr in dem Format ein: I20250001");
            return true;
        }
        return false;
    }


    private Boolean isNotValidArticleNumber(String articleNumber) {
        return !articleNumber.matches("^A\\d{8}$");
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

    private boolean isNotValidCustomerNumber(String customerNumber) {
        if (!customerNumber.matches("^C\\d{8}$")) {
            AlertService.showErrorAlert("Bitte geben Sie eine Kunden-Nr in dem Format ein: C20250001");
            return true;
        }
        return false;
    }

}
