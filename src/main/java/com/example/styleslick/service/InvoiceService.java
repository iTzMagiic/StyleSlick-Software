package com.example.styleslick.service;

import com.example.styleslick.model.Database;
import com.example.styleslick.model.Invoice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceService {

    private static InvoiceService invoiceService;
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
        Map<String, String> filledFields = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() == null || !entry.getValue().trim().isEmpty()) {
                continue;
            }

            filledFields.put(entry.getKey(), entry.getValue());
        }

        System.out.println("test");
        return true;
    }


    public void clearSession() {
        database = null;
        invoiceService = null;
    }
}
