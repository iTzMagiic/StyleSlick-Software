package com.example.styleslick.model;

import com.example.styleslick.utils.SceneManager;

import java.util.List;

public class CustomerService {

    private static CustomerService customerService;
    private Database database;

    /* TODO: CustomerService muss Database.existsCustomer() Methode enthalten.
        Methode muss prüfen ob bei den Eingaben ein oder Mehrere passenden Kunden gefunden worden.
     */

    private CustomerService() {}

    public CustomerService(Database database) {
        this.database = database;
    }

    public static CustomerService getInstance() {
        if (customerService == null) {
            customerService = new CustomerService();
        }
        return customerService;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }



    public void searchCustomer(String columnName, String value) {
        List<Customer> listOfCustomers = database.searchCustomerOneParameter(columnName, value);



        // TODO: searCustomer Methode muss an ein Nues Fenster weiterleiten das nur die gesuchten Kunden ausgibt
        SceneManager.switchScene("path// foundCustomers-view.fxml", "Gefundene Kunden");
    }


}
