package com.example.styleslick.service;

import com.example.styleslick.model.Customer;
import com.example.styleslick.model.Database;
import com.example.styleslick.model.Rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerService {

    private static CustomerService customerService;
    private Database database;


    private CustomerService() {}

    public static CustomerService getInstance() {
        if (customerService == null) {
            customerService = new CustomerService();
        }
        return customerService;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }


    public void addCustomer(Map<String, String> fields) {
        // Prüfen ob Felder Leer sind wenn ja Fehlermeldung ausgeben
        /* TODO:: in der Datenbank überprüfen welche Felder überhaupt ausgefüllt werden müssen
            Evtl. die Map weiterleiten an die Klasse Rules das er Prüft ob die Felder die ausgefüllt werden
            müssen auch ausgefüllt sind.
            - Fehler für jeden bestimmtes Feld ausgeben
            Wenn der Rest passt weiterleiten an die Datenbank und Kunden erstellen.

         */
    }


    // Sucht nach den Kunden und gibt eine Liste von Customer wieder
    public List<Customer> searchCustomer(Map<String, String> fields) {
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        if (filledFields.isEmpty()) {
            Rules.showErrorAlert("Bitte mindestens Ein Feld ausfüllen.");
            return null;
        }

        return database.searchCustomer(filledFields);
    }

    // Liste aller Customers wiedergeben
    public List<Customer> getCustomers() {
        return database.getAllCustomers();
    }


}
