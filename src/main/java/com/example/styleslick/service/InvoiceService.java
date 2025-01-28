package com.example.styleslick.service;

import com.example.styleslick.model.Database;
import com.example.styleslick.model.Invoice;
import com.example.styleslick.model.InvoiceItem;
import com.example.styleslick.rules.InvoiceRules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceService {

    private static InvoiceService invoiceService;
    private InvoiceRules invoiceRules = new InvoiceRules();
    private Database database;


    private InvoiceService() {
    }


    public static InvoiceService getInstance() {
        if (invoiceService == null) {
            invoiceService = new InvoiceService();
        }
        return invoiceService;
    }


    public void setDatabase(Database database) {
        this.database = database;
    }


    public Database getDatabase() {
        return database;
    }


    public List<Invoice> getAllInvoices() {
        return database.getAllInvoices();
    }


    public boolean addInvoice(Map<String, String> invoiceFields, Map<String, String> itemFields) {
        Map<String, String> filledInvoiceFields = new HashMap<>();
        Map<String, String> filledItemFields = new HashMap<>();

        for (Map.Entry<String, String> entry : invoiceFields.entrySet()) {
            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                continue;
            }
            filledInvoiceFields.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, String> entry : itemFields.entrySet()) {
            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                continue;
            }

            filledItemFields.put(entry.getKey(), entry.getValue());
        }


        if (invoiceRules.isNotAllowedToAddItemToInvoice(filledItemFields)) {
            return false;
        }

        if (filledInvoiceFields.containsKey("shipping_cost")) {
            filledInvoiceFields.replace("shipping_cost", filledInvoiceFields.get("shipping_cost").replace(",", "."));
        }

        if (filledInvoiceFields.containsKey("payment_amount")) {
            filledInvoiceFields.replace("payment_amount", filledInvoiceFields.get("payment_amount").replace(",", "."));
        }

        if (invoiceRules.isNotAllowedToAddInvoice(filledInvoiceFields)) {
            return false;
        }


        if (database.getCustomerID(filledInvoiceFields.get("customer_id")) == -1) {
            AlertService.showErrorAlert("Kunden-Nr existiert nicht.");
            return false;
        }


        int stockOfArticle = database.getStockOfArticle(Integer.parseInt(filledItemFields.get("article_id")));

        if (stockOfArticle == -9999) {
            AlertService.showErrorAlert("Es konnte kein Artikel mit der Artikel-Nr gefunden werden.");
            return false;
        }

        if (stockOfArticle <= 0) {
            AlertService.showErrorAlert("Der Bestand des Artikel ist " + stockOfArticle + ".");
            return false;
        }
        if (stockOfArticle < Integer.parseInt(filledItemFields.get("amount"))) {
            AlertService.showErrorAlert("Zu wenig Artikel im Bestand.");
            return false;
        }


        if (database.addInvoice(filledInvoiceFields, filledItemFields)) {
            AlertService.showConfirmAlert("Die Bestellung wurde erfolgreich erstellt.");
            return true;
        }
        else {
            AlertService.showErrorAlert("Die Bestellung konnte nicht erstellt werden.");
            return false;
        }
    }


    public boolean addItemToInvoiceWithInvoiceID(Map<String, String> itemFields, int invoice_id) {
        Map<String, String> filledItemFields = new HashMap<>();

        for (Map.Entry<String, String> entry : itemFields.entrySet()) {
            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                continue;
            }
            filledItemFields.put(entry.getKey(), entry.getValue());
        }


        if (invoiceRules.isNotAllowedToAddItemToInvoice(itemFields)) {
            return false;
        }


        int stockOfArticle = database.getStockOfArticle(Integer.parseInt(filledItemFields.get("article_id")));

        if (stockOfArticle == -9999) {
            AlertService.showErrorAlert("Es konnte kein Artikel mit der Artikel-Nr gefunden werden.");
            return false;
        }

        if (stockOfArticle <= 0) {
            AlertService.showErrorAlert("Der Bestand des Artikel ist " + stockOfArticle + ".");
            return false;
        }
        if (stockOfArticle < Integer.parseInt(filledItemFields.get("amount"))) {
            AlertService.showErrorAlert("Zu wenig Artikel im Bestand.");
            return false;
        }


        if (database.addItemToInvoice(invoice_id, filledItemFields)) {
            AlertService.showConfirmAlert("Der Artikel wurde erfolgreich hinzugefügt.");
            return true;
        } else {
            AlertService.showErrorAlert("Der Artikel konnte nicht zu der Bestellung hinzugefügt werden.");
            return false;
        }
    }


    public boolean addItemToInvoiceWithInvoiceNumber(Map<String, String> filledFields, String invoice_number) {

        int invoiceID = database.getInvoiceID(invoice_number);

        if (invoiceID == -1) {
            AlertService.showErrorAlert("Es wurde keine Bestellung mit der Bestell-Nr: " + invoice_number + " gefunden.");
            return false;
        }

        if (!addItemToInvoiceWithInvoiceID(filledFields, invoiceID)) {
            return false;
        }

        return true;
    }


    public boolean deleteInvoice(int invoiceID) {
        if (!database.deleteInvoice(invoiceID)) {
            AlertService.showErrorAlert("Fehler beim Löschen der Bestellung.");
            return false;
        }

        AlertService.showConfirmAlert("Die Bestellung wurde erfolgreich gelöscht.");
        return true;
    }


    public List<InvoiceItem> getInvoiceItems(int invoice_id) {
        List<InvoiceItem> listOfInvoiceItems;

        listOfInvoiceItems = database.getInvoiceItems(invoice_id);

        if (listOfInvoiceItems.isEmpty()) {
            AlertService.showErrorAlert("Es wurden keine Bestellten Artikel gefunden.");
        }

        return listOfInvoiceItems;
    }


    public void clearSession() {
        database = null;
        invoiceService = null;
    }
}
