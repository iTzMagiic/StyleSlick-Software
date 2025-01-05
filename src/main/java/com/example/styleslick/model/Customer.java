package com.example.styleslick.model;

public class Customer {
    private final int customerID;
    private final String username;
    private final String name;
    private final String last_name;
    private final String street;
    private final int postal_code;
    private final String city;
    private final String purchased_from;

    public Customer(String username, String name, String last_name, String street, int postal_code, String city, String purchased_from, int customer_id) {
        this.username = username;
        this.name = name;
        this.last_name = last_name;
        this.street = street;
        this.postal_code = postal_code;
        this.city = city;
        this.purchased_from = purchased_from;
        this.customerID = customer_id;
    }

    public String getUsername() {
        return username;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getStreet() {
        return street;
    }

    public int getPostalCode() {
        return postal_code;
    }

    public String getCity() {
        return city;
    }

    public String getPurchasedFrom() {
        return purchased_from;
    }

    @Override
    public String toString() {
        return "Benutzername: " + username + "\n Name: " + name + "\n Nachname: " + last_name + "\n Straße: " + street + "\n Postleitzahl: " + postal_code + "\n Ort: " + city + "\n Gekauft über: " + purchased_from;
    }
}
