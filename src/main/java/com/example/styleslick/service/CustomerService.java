package com.example.styleslick.service;

import com.example.styleslick.model.Customer;
import com.example.styleslick.model.Database;
import com.example.styleslick.rules.CustomerRules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerService {

    private static final CustomerRules customerRules = new CustomerRules();
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


        if (customerRules.isNotAllowedToAddCustomer(filledFields)) {
            return false;
        }


        if (database.isUsernameExist(filledFields.get("username"))) {
            if (!AlertService.showConfirmAlertResult("Möchte Sie wirklich noch einen Kunden mit dem selben Benutzernamen erstellen? '" + filledFields.get("username") + "'")) {
                return false;
            }
        }


        if (!database.addCustomer(filledFields)) {
            AlertService.showErrorAlert("Fehler beim speichern des Kunden, in die Datenbank.");
            return false;
        }


        AlertService.showConfirmAlert("Der Kunde wurde erfolgreich angelegt.");
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


        if (customerRules.isNotAllowedToSearchCustomer(filledFields)) {
            return listOfCustomers;
        }

        listOfCustomers = database.searchCustomerLike(filledFields);

        if (listOfCustomers == null || listOfCustomers.isEmpty()) {
            AlertService.showErrorAlert("Kein passender Kunde gefunden.");
        }

        return listOfCustomers;
    }


    public boolean updateCustomer(Map<String, String> fields, Customer customer) {
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }


        if (customerRules.isNotAllowedToUpdateCustomer(filledFields)) {
            return false;
        }


        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich den Kunden mit der Kunden-Nr: " + customer.getCustomerNumber() + " bearbeiten?")) {
            AlertService.showErrorAlert("Kunde wird nicht bearbeitet.");
            return false;
        }


        if (filledFields.containsKey("customer_number") && !filledFields.get("customer_number").equals(customer.getCustomerNumber())) {
            if (database.isCustomerNumberExist(fields.get("customer_number"))) {
                AlertService.showErrorAlert("Die Kunden-Nr existiert bereits.");
                return false;
            }
        }


        if (!database.updateCustomer(filledFields, customer.getCustomerID())) {
            AlertService.showErrorAlert("Fehler beim bearbeiten des Kunden.");
            return false;
        }


        AlertService.showConfirmAlert("Der Kunde wurde erfolgreich bearbeitet.");
        return true;
    }


    public boolean deleteCustomer(Customer customer) {

        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich den Kunden mit der Kunden-Nr: '" + customer.getCustomerNumber() + "' löschen?")) {
            AlertService.showErrorAlert("Kunde wird nicht gelöscht.");
            return false;
        }

        if (database.customerHasInvoices(customer.getCustomerID())) {
            AlertService.showErrorAlert("Bitte löschen Sie zuerst alle Bestellungen des Kunden, bevor Sie ihn entfernen.");
            return false;
        }

        if (!database.deleteCustomer(customer.getCustomerID())) {
            AlertService.showErrorAlert("Kunde konnte nicht gelöscht werden.");
            return false;
        }

        AlertService.showConfirmAlert("Kunde erfolgreich gelöscht.");
        return true;
    }


    public List<Customer> getCustomers() {
        return database.getAllCustomers();
    }


    public void clearSession() {
        database = null;
        customerService = null;
    }
}
