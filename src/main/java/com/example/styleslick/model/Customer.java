package com.example.styleslick.model;

public class Customer {
    final String username;
    final String name;
    final String lastName;
    final String adress;
    final int plz;
    final String platform;

    public Customer(String username, String name, String lastName, String adress, int plz, String platform) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.adress = adress;
        this.plz = plz;
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
    public String getAdress() {
        return adress;
    }
    public int getPlz() {
        return plz;
    }
    public String getPlatform() {
        return platform;
    }

    @Override
    public String toString() {
        return "Benutzername: " + username + "\n Name: " + name + "\n Nachname: " + lastName + "\n Adress: " + adress + "\n Plz: " + plz + "\n Platform: " + platform;
    }
}
