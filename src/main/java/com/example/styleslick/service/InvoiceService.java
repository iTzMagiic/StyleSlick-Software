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

    public boolean addInvoice(Map<String, String> fields) {
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                continue;
            }

            filledFields.put(entry.getKey(), entry.getValue());
        }


        if (filledFields.containsKey("shipping_cost")) {
            filledFields.replace("shipping_cost", filledFields.get("shipping_cost").replace(",", "."));
        }

        if (filledFields.containsKey("payment_amount")) {
            filledFields.replace("payment_amount", filledFields.get("payment_amount").replace(",", "."));
        }

        if (invoiceRules.isNotAllowedToAddInvoice(filledFields)) {
            return false;
        }

        AlertService.showConfirmAlert("Bestellung wurde erfolgreich aufgenommen.");
        return true;
    }


    public void clearSession() {
        database = null;
        invoiceService = null;
    }
}
