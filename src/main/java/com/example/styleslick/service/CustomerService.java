package com.example.styleslick.service;

import com.example.styleslick.model.Customer;
import com.example.styleslick.model.Database;

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


    public boolean addCustomer(Map<String, String> fields) {
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {continue;}

            filledFields.put(entry.getKey(), entry.getValue());
        }

        // Prüft, ob die pflicht Felder nicht leer sind.
        if (!filledFields.containsKey("benutzername") || filledFields.get("benutzername") == null || filledFields.get("benutzername").isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie einen Benutzernamen an.");
            return false;
        } else if (!filledFields.containsKey("gekauft_ueber") || filledFields.get("gekauft_ueber") == null || filledFields.get("gekauft_ueber").isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie eine Platform über der Gekauft wurden ist an.");
            return false;
        }

        if (database.isUsernameExist(filledFields.get("benutzername"))) {
            //TODO:: Noch eine Abfrage erstellen mit der Klasse RulesService ob man wirklich ein neuen Kunden erstellen
            //  möchte mit den selben Benutzername
            RulesService.showErrorAlert("Benutzername existiert bereits.");
            return false;
        }

        database.addCustomer(filledFields);
        RulesService.showConfirmAlert("Kunde wurde erfolgreich angelegt.");
        return true;
    }


    // Sucht nach den Kunden und gibt eine Liste von Customer wieder
    public List<Customer> searchCustomer(Map<String, String> fields) {
        Map<String, String> filledFields = new HashMap<>();
        List<Customer> listOfCustomers;

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte mindestens Ein Feld ausfüllen.");
            return null;
        }

        listOfCustomers = database.searchCustomer(filledFields);

        // Sucht in der Datenbank alles, was mit der Eingabe anfängt wenn kein Kunde gefunden wurden ist.
        //      WHERE (column) LIKE "B%";
        if (listOfCustomers == null || listOfCustomers.isEmpty()) {
            listOfCustomers = database.searchCustomerLike(filledFields);
        }

        if (listOfCustomers == null || listOfCustomers.isEmpty()) {
            RulesService.showErrorAlert("Kein Kunden gefunden.");
        }
        return listOfCustomers;
    }

    // Liste aller Customers wiedergeben
    public List<Customer> getCustomers() {
        return database.getAllCustomers();
    }


    public void clearSession() {
        database = null;
        customerService = null;
    }


}
