package com.example.styleslick.model;

public class CustomerService {

    final Database database;

    CustomerService(Database database) {
        this.database = database;
    }

    public boolean searchCustomer(String columnName, String username, String name, String lastName, String address, int plz, String platform) {


    }

    // Die Methode gibt die Anzahl an benutzern zurück dies mit den selben Werten gibt
    public int getNumberOfCustomers(String columnName, String value) {
        //Prüfen ob im value buchstaben für die PLZ drin steht
        if (value.matches("[1-9]")) {
            return database.existsCustomerInt(columnName, Integer.parseInt(value));
        }
        return database.existsCustomerString(columnName, value);
    }

}
