package com.example.styleslick.service;

import com.example.styleslick.model.Database;
import com.example.styleslick.model.Invoice;
import com.example.styleslick.rules.InvoiceRules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceService {

    private static InvoiceService invoiceService;
    private InvoiceRules invoiceRules = new InvoiceRules();
    private Database database;



    private InvoiceService() {}



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

        if (addInvoiceItem(filledItemFields, database.addInvoice(filledInvoiceFields))) {
            return true;
        }


        AlertService.showConfirmAlert("Bestellung wurde erfolgreich aufgenommen.");
        return true;
    }


    public boolean addInvoiceItem(Map<String, String> itemFields, int invoice_id) {
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


        return true;
    }


    public void clearSession() {
        database = null;
        invoiceService = null;
    }
}
