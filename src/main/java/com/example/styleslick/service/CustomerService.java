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

        // Prüft, ob die pflicht Felder nicht leer sind.
        if (!filledFields.containsKey("benutzername") || filledFields.get("benutzername") == null || filledFields.get("benutzername").trim().isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie einen Benutzernamen an.");
            return false;
        }
        if (!filledFields.containsKey("gekauft_ueber") || filledFields.get("gekauft_ueber") == null || filledFields.get("gekauft_ueber").trim().isEmpty()) {
            RulesService.showErrorAlert("Bitte geben Sie eine Platform über der Gekauft wurden ist an.");
            return false;
        }
        if (filledFields.containsKey("plz")) {
            try {
                Integer.parseInt(filledFields.get("plz")); // Versuche nur, den Wert zu parsen
                return true;
            } catch (NumberFormatException e) {
                RulesService.showErrorAlert("Postleitzahl darf nur aus Zahlen bestehen.");
                return false;
            }
        }

        if (database.isUsernameExist(filledFields.get("benutzername"))) {
            if (!RulesService.showConfirmAlertResult("Möchte Sie wirklich noch einen Kunden mit dem selben Benutzernamen erstellen? '" + filledFields.get("benutzername") + "'")) {
                return false;
            }
        }

        database.addCustomer(filledFields);
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

        listOfCustomers = database.searchCustomerLike(filledFields);

        if (listOfCustomers == null || listOfCustomers.isEmpty()) {
            RulesService.showErrorAlert("Kein Kunden gefunden.");
        }
        return listOfCustomers;
    }


    public boolean deleteCustomer(int customerID) {
        if (RulesService.showConfirmAlertResult("Möchten Sie wirklich den Kunden mit der Kunden Nummer '" + customerID + "' löschen?")) {
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
