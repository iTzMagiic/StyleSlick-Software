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

    public Connection getConnection() {
        Connection connection = null;

        try {
            // Hier verwenden wir die Methode aus der DatabaseConnection-Klasse
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            if (connection != null) {
                return connection;
            }
        } catch (SQLException e) {
            System.err.println("Verbindung zur Datenbank fehlgeschlagen! " + e.getMessage());
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Schließen der Datenbank fehlgeschlagen! " + e.getMessage());
                }
            }
        }
        return connection;
    }

    public boolean testConnection() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (connection != null) {
                return true; // Verbindung erfolgreich
            }
        } catch (SQLException e) {
            System.err.println("Verbindung zur Datenbank fehlgeschlagen! " + e.getMessage());
        }
        return false; // Verbindung fehlgeschlagen
    }

    public boolean addBook(String title, String author, int yearOfPublication, int userID) {
        String sql = "INSERT INTO buecher (titel, autor, erscheinungsjahr, idbenutzer) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, yearOfPublication);
            preparedStatement.setInt(4, userID);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Fehler beim Hinzufügen des Buches: " + e.getMessage());
        }
        return false;
    }

    public List<Book> getAllBooks(int userID) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT titel, autor, erscheinungsjahr FROM buecher WHERE idbenutzer = ?";
        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("titel");
                String author = resultSet.getString("autor");
                int erscheinungsjahr = resultSet.getInt("erscheinungsjahr");

                Book book = new Book(title, author, erscheinungsjahr);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Abrufen der Bücher: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Fehler beim Schließen des Resultsets: " + e.getMessage());
                }
            }
        }
        return books;
    }

    public boolean removeBook(String title, int userID) {
        String sql = "DELETE FROM buecher WHERE titel = ? AND idbenutzer = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, userID);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Fehler beim Löschen des Buches: " + e.getMessage());
        }

        return false;
    }

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
