package com.example.styleslick.service;

import com.example.styleslick.model.Database;
import com.example.styleslick.model.Invoice;

import java.util.List;

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


    public void clearSession() {
        database = null;
        invoiceService = null;
    }
}
