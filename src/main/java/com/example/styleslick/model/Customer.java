package com.example.styleslick.model;

public class Customer {
    private final int customerID;
    private final String customer_number;
    private final String username;
    private final String first_name;
    private final String last_name;
    private final String street;
    private final int postal_code;
    private final String city;
    private final String country;
    private final String purchased_from;

    public Customer(String username, String first_name, String last_name, String street, int postal_code, String city, String country, String purchased_from, int customer_id, String customer_number) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.street = street;
        this.postal_code = postal_code;
        this.city = city;
        this.country = country;
        this.purchased_from = purchased_from;
        this.customerID = customer_id;
        this.customer_number = customer_number;
    }


    public String getCountry() {
        return this.country;
    }

    public String getCustomerNumber() {
        return this.customer_number;
    }

    public String getUsername() {
        return username;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getFirstName() {
        return first_name;
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
        return "Benutzername: " + username + "\n Name: " + first_name + "\n Nachname: " + last_name + "\n Straße: " + street + "\n Postleitzahl: " + postal_code + "\n Ort: " + city + "\n Gekauft über: " + purchased_from;
    }
}
