package com.example.styleslick.model;

public class Customer {
    private final int customer_id;
    private final String username;
    private final String name;
    private final String lastName;
    private final String street;
    private final int plz;
    private final String ort;
    private final String platform;

    public Customer(String username, String name, String lastName, String street, int plz, String ort, String platform, int customer_id) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.street = street;
        this.plz = plz;
        this.ort = ort;
        this.platform = platform;
        this.customer_id = customer_id;
    }

    public String getUsername() {
        return username;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreet() {
        return street;
    }

    public int getPlz() {
        return plz;
    }

    public String getOrt() {
        return ort;
    }

    public String getPlatform() {
        return platform;
    }

    @Override
    public String toString() {
        return "Benutzername: " + username + "\n Name: " + name + "\n Nachname: " + lastName + "\n Strasse: " + street + "\n Plz: " + plz + "\n Ort: " + ort + "\n Platform: " + platform;
    }
}
