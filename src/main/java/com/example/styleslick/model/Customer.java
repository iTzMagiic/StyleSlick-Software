package com.example.styleslick.model;

public class Customer {
    private final String username;
    final String name;
    final String lastName;
    final String street;
    final int plz;
    final String ort;
    final String platform;

    public Customer(String username, String name, String lastName, String street, int plz, String ort, String platform) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.street = street;
        this.plz = plz;
        this.ort = ort;
        this.platform = platform;
    }

    public String getUsername() {
        return username;
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
