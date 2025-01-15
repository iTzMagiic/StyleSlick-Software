package com.example.styleslick.service;

import com.example.styleslick.model.Database;

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


    public void clearSession() {
        database = null;
        invoiceService = null;
    }
}
