package com.example.styleslick.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String URL = "jdbc:mysql://localhost:3306/styleslickdb";

    private final String USER;
    private final String PASSWORD;

    public Database(String USER, String PASSWORD) {
        this.USER = USER;
        this.PASSWORD = PASSWORD;

    }

    // Gibt die Verbindung zur Datenbank wieder
    public Connection getConnection() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return connection;
        } catch (SQLException e) {
            System.err.println("Verbindung zur Datenbank fehlgeschlagen! " + e.getMessage());
        }
        return null;
    }

    // Testet die Verbindung zur Datenbank
    public boolean testConnection() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (connection != null) {
                return true; // Verbindung erfolgreich
            }
        } catch (SQLException e) {
            System.err.println("Verbindung zur Datenbank fehlgeschlagen! " + e.getMessage());
        }
        Rules.showErrorAlert("Verbindung zur Datenbank fehlgeschlagen!");
        return false; // Verbindung fehlgeschlagen
    }

    // Gibt "true" wieder wenn man erfolgreich ein Kunden hinzugefügt hat und "false" wenn nicht
    public boolean addCustomer(String username, String name, String lastName, String address, int plz, String platform) {
        String sql = "INSERT INTO buecher (titel, autor, erscheinungsjahr, idbenutzer) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, address);
            preparedStatement.setInt(5, plz);
            preparedStatement.setString(6, platform);
            preparedStatement.executeUpdate();
            Rules.showConfirmAlert("Neuer Benutzer wurde hinzugefügt.");
            return true;
        } catch (SQLException e) {
            System.out.println("Fehler beim Hinzufügen des Buches: " + e.getMessage());
        }
        Rules.showErrorAlert("Fehler beim hinzufügen des Benutzer.");
        return false;
    }

    // Gibt alle bestehenden Kunden in einer Liste wieder
    public List<Customer> getAllCustomers() {
        List<Customer> listOfCustomers = new ArrayList<>();
        String sql = "SELECT benutzername, name, nachname, adresse, plz, plattform FROM kunden";
        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("benutzername");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("nachname");
                String adresse = resultSet.getString("adresse");
                int plz = resultSet.getInt("plz");
                String platform = resultSet.getString("plattform");

                Customer customer = new Customer(username, name, lastName, adresse, plz, platform);
                listOfCustomers.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Abrufen der Kunden: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Fehler beim Schließen des Resultsets: " + e.getMessage());
                }
            }
        }
        return listOfCustomers;
    }

//    // Gibt die Anzahl an vorkommenden Benutzernamen wieder.
//    public int existCustomerUsername(String username) {
//        String sql = "SELECT benutzername FROM kunden WHERE benutzername = ?";
//        ResultSet resultSet = null;
//        int count = 0;
//
//        try (Connection connection = getConnection();
//        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, username);
//            resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                count++;
//            }
//            return count;
//        } catch (SQLException e) {
//            System.err.println("Fehler beim Verbinden zur Datenbank. " + e.getMessage());
//        } finally {
//            if (resultSet != null) {
//                try {
//                    resultSet.close();
//                } catch (SQLException e) {
//                    System.err.println("Fehler beim schließen des ResultSets: " + e.getMessage());
//                }
//            }
//        }
//        return count;
//    }
//
//    public int existCustomerName(String name) {
//        String sql = "SELECT name FROM kunden WHERE name = ?";
//        ResultSet resultSet = null;
//        int count = 0;
//
//        try (Connection connection = getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, name);
//            resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                count++;
//            }
//            return count;
//        } catch (SQLException e) {
//            System.err.println("Fehler beim Verbinden zur Datenbank. " + e.getMessage());
//        } finally {
//            if (resultSet != null) {
//                try {
//                    resultSet.close();
//                } catch (SQLException e) {
//                    System.err.println("Fehler beim schließen des ResultSets: " + e.getMessage());
//                }
//            }
//        }
//        return count;
//    }

    // Gibt die Anzahl von Kunden wieder
    public int existsCustomerString(String columnName, String value) {
        String sql = "SELECT " + columnName + " FROM kunden WHERE " + columnName + " = ?";

        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, value);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                int count = 0;
                while (resultSet.next()) {
                    count++;
                }
                return count;
            } catch (SQLException e) {
                System.err.println("Fehler beim schließen des ResultSets: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Verbinden zur Datenbank. " + e.getMessage());
        }
        return 0;
    }

    // Gibt die Anzahl von Kunden mit der selben PLZ wieder
    public int existsCustomerInt(String columnName, int value) {
        String sql = "SELECT " + columnName + " FROM kunden WHERE " + columnName + " = ?";

        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, value);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int count = 0;

                while (resultSet.next()) {
                    count++;
                }
            } catch (SQLException e) {
                System.err.println("Fehler beim schließen des ResultSets: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Verbinden zur Datenbank. " + e.getMessage());
        }
        return 0;
    }






//    public boolean searchCustomer(String title, int userID) {
//        String sql = "DELETE FROM buecher WHERE titel = ? AND idbenutzer = ?";
//
//        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, title);
//            preparedStatement.setInt(2, userID);
//            preparedStatement.executeUpdate();
//
//            return true;
//        } catch (SQLException e) {
//            System.out.println("Fehler beim Löschen des Buches: " + e.getMessage());
//        }
//
//        return false;
//    }

    public int getUserID(String username, String password) {
        String sql = "SELECT idbenutzer FROM benutzer WHERE benutzername = ? AND passwort = ?";
        int userID = 0;
        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userID = resultSet.getInt("idbenutzer");
                return userID;
            } else {
                return -1;
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Abrufen der userID: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Fehler beim Schließen des ResultSets: " + e.getMessage());
                }
            }
        }

        return -1;
    }

    public boolean createAccount(String name, String username, String password) {
        String sql = "INSERT INTO benutzer (name, benutzername, passwort) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            // executeUpdate() wird verwendet, um Daten zu verändern (INSERT, UPDATE, DELETE-Anweisung)
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Fehler beim erstellen des Accounts: " + e.getMessage());
        }
        return false;
    }

    public String getName(int userID) {
        String sql = "SELECT name FROM benutzer WHERE idbenutzer = ?";
        String name;
        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userID);
            // executeQuery() wird verwendet, um Daten abzufragen (SELECT-Anweisung)
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // ResultSet kann man sich wie ein Assoziatives Array Vorstellen, was alle Zeilen meiner Abfrage
                // von preparedStatement.executeQuery() abspeichert und man mit dem key "name" das, Value dort drin
                // ausgibt, unten ein beispiel wie es aussieht in einem ResultSet
                /*
                idbenutzer	    name	        email
                1	            Alice	    alice@mail.com
                2	            Bob	        bob@mail.com
                 */
                name = resultSet.getString("name");

//                // Ersten Buchstaben des Namen in groß umwandeln
//                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                return name;
            }

        } catch (SQLException e) {
            System.out.println("Fehler bei Abrufen des Namens: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Fehler bei Schließen des ResultSets: " + e.getMessage());
                }
            }
        }
        return "no user";
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT benutzername FROM benutzer WHERE benutzername = ?";
        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Abrufen des Benutzernamen: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Fehler beim Schließen des ResultSets: " + e.getMessage());
                }
            }
        }
        return false;
    }

    public boolean bookExists(String title, int userID) {
        String sql = "SELECT titel FROM buecher WHERE titel = ? AND idbenutzer = ?";
        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, userID);
            resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Fehler beim Prüfen nach dem Buch: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Fehler beim Schließen des Resultsets: " + e.getMessage());
                }
            }
        }

        return false;
    }

}
