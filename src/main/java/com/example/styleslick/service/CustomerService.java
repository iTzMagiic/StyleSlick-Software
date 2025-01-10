package com.example.styleslick.service;

import com.example.styleslick.model.Customer;
import com.example.styleslick.model.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerService {

    private static CustomerService customerService;
    private Database database;


    private CustomerService() {
    }


    public static synchronized CustomerService getInstance() {
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
            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                continue;
            }
            filledFields.put(entry.getKey(), entry.getValue());
        }

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie was ein.");
            return false;
        }

        // Prüft, ob die pflicht Felder nicht leer sind.
        if (!filledFields.containsKey("username")) {
            RulesService.showErrorAlert("Bitte geben Sie einen Benutzernamen an.");
            return false;
        }
        if (!filledFields.containsKey("purchased_from")) {
            RulesService.showErrorAlert("Bitte geben Sie eine Platform über der Gekauft wurden ist an.");
            return false;
        }
        if (filledFields.containsKey("postal_code") && !filledFields.get("postal_code").matches("\\d{5}")) {
            RulesService.showErrorAlert("Die Postleitzahl darf nur aus Zahlen bestehen und muss 5-stellig sein.");
            return false;
        }
        if (!filledFields.containsKey("country") || filledFields.containsKey("country") && !filledFields.get("country").matches("^[a-zA-Z]+$")) {
            RulesService.showErrorAlert("Bitte geben Sie ein Land ein.");
            return false;
        }

        if (database.isUsernameExist(filledFields.get("username"))) {
            if (!RulesService.showConfirmAlertResult("Möchte Sie wirklich noch einen Kunden mit dem selben Benutzernamen erstellen? '" + filledFields.get("username") + "'")) {
                return false;
            }
        }

        if (!database.addCustomer(filledFields)) {
            RulesService.showErrorAlert("Fehler beim speichern in die Datenbank.");
            return false;
        }
        RulesService.showConfirmAlert("Kunde wurde erfolgreich angelegt.");
        return true;
    }


    public List<Customer> searchCustomer(Map<String, String> fields) {
        Map<String, String> filledFields = new HashMap<>();
        List<Customer> listOfCustomers = new ArrayList<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte mindestens Ein Feld ausfüllen.");
            return listOfCustomers;
        }

        if (filledFields.containsKey("postal_code") && !filledFields.get("postal_code").matches("\\d{5}")) {
            RulesService.showErrorAlert("Die Postleitzahl darf nur aus Zahlen bestehen und muss 5-stellig sein.");
            return listOfCustomers;
        }

        listOfCustomers = database.searchCustomer(filledFields);

        if (listOfCustomers == null || listOfCustomers.isEmpty()) {
            RulesService.showErrorAlert("Kein Kunden gefunden.");
        }
        return listOfCustomers;
    }


    public boolean updateCustomer(Map<String, String> fields, int customerID) {
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie etwas an um den Kunden zu bearbeiten.");
            return false;
        }

        if (!RulesService.showConfirmAlertResult("Möchten Sie wirklich den Kunden mit der Kunden-Nr: " + customerID + " bearbeiten?")) {
            RulesService.showErrorAlert("Kunde wird nicht bearbeitet.");
            return false;
        }

        if (filledFields.containsKey("postal_code") && !filledFields.get("postal_code").matches("\\d{5}")) {
            RulesService.showErrorAlert("Die Postleitzahl darf nur aus Zahlen bestehen und muss 5-stellig sein.");
            return false;
        }

        if (!database.updateCustomer(filledFields, customerID)) {
            RulesService.showErrorAlert("Fehler beim bearbeiten des Kunden.");
            return false;
        }

        RulesService.showConfirmAlert("Der Kunde wurde erfolgreich bearbeitet.");
        return true;
    }


    public boolean deleteCustomer(int customerID) {
        if (RulesService.showConfirmAlertResult("Möchten Sie wirklich den Kunden mit der Kunden-Nr: '" + customerID + "' löschen?")) {
            return database.deleteCustomer(customerID);
        }
        return false;
    }


    public List<Customer> getCustomers() {
        return database.getAllCustomers();
    }


    public void clearSession() {
        database = null;
        customerService = null;
    }
}
