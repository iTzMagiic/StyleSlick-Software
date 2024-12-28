package com.example.styleslick.model;

import com.example.styleslick.service.RulesService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Database {

    private final String URL = "jdbc:mysql://localhost:3306/styleslickdb";
    private final String USER;
    private final String PASSWORD;


    public Database(String USER, String PASSWORD) {
        this.USER = USER;
        this.PASSWORD = PASSWORD;

    }


    public boolean isConnected() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (connection != null) {
                return true; // Verbindung erfolgreich
            }
        } catch (SQLException e) {
            System.err.println("Verbindung zur Datenbank fehlgeschlagen! " + e.getMessage());
        }
        return false; // Verbindung fehlgeschlagen
    }


    public void addCustomer(Map<String, String> filledFields) {
        String sql = "INSERT INTO customer (";
        StringBuilder whereClause = new StringBuilder();

        // WHERE-Bedingung aus der Map filledFields einfügen und mit(" AND " &" = ?")trennen;
        for (Map.Entry<String, String> key : filledFields.entrySet()) {
            String columnName = key.getKey();

            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append(columnName);
        }
        whereClause.append(") VALUES (");
        sql += whereClause.toString();


        whereClause = new StringBuilder();

        for (Map.Entry<String, String> values : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append(" ?");
        }

        whereClause.append(")");
        sql += whereClause.toString();


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1; // <- index verweist auf die Fragezeichen in der SQL Abfrage (?, ?, ?, ?)

            for (Map.Entry<String, String> field : filledFields.entrySet()) {
                if (field.getKey().equals("plz")) {
                    preparedStatement.setInt(index++, Integer.parseInt(field.getValue()));
                } else {
                    preparedStatement.setString(index++, field.getValue());
                }
            }
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            RulesService.showErrorAlert("Fehler beim hinzufügen des Kunden.");
            System.err.println("Fehler beim Verbinden zur Datenbank. " + e.getMessage());
        }
    }


    public boolean isUsernameExist(String username) {
        String sql = "SELECT * FROM customer WHERE benutzername = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim abrufen ob der Benutzername schon existiert. " + e.getMessage());
            return false;
        }
        return false;
    }


    // Gibt alle bestehenden Kunden in einer Liste wieder
    public List<Customer> getAllCustomers() {
        List<Customer> listOfCustomers = new ArrayList<>();
        String sql = "SELECT * FROM customer";


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("benutzername");
                    String name = resultSet.getString("name");
                    String lastName = resultSet.getString("nachname");
                    String street = resultSet.getString("strasse");
                    int plz = resultSet.getInt("plz");
                    String ort = resultSet.getString("ort");
                    String platform = resultSet.getString("gekauft_ueber");
                    int customer_id = resultSet.getInt("customer_id");

                    Customer customer = new Customer(username, name, lastName, street, plz, ort, platform, customer_id);
                    listOfCustomers.add(customer);
                }
            }

        } catch (SQLException e) {
            System.err.println("Fehler beim Abrufen der Kunden: " + e.getMessage());
        }
        return listOfCustomers;
    }


    // Gibt eine Liste der Zutreffenden Kunden wieder wen, nur eine Eingabe getätigt wurde
    public List<Customer> searchCustomer(Map<String, String> filledFields) {
        // Liste in der Die gefundenen Kunden eingetragen werden
        List<Customer> listOfCustomers = new ArrayList<>();

        // SQL Abfrage für die Datenbank
        String sql = "SELECT * FROM customer WHERE ";

        // WHERE-Bedingungen zum hinzufügen
        StringBuilder whereClause = new StringBuilder();

        // WHERE-Bedingung aus der Map filledFields einfügen und mit(" AND " &" = ?")trennen;
        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            String columnName = entry.getKey();

            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(columnName).append(" = ?");
        }

        // die sql Abfrage wird mit den WHERE-Bedingungen erweitern
        sql += whereClause.toString();


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int index = 1; // <- index verweist auf die Fragezeichen in der SQL Abfrage (?, ?, ?, ?)
            //
            for (String value : filledFields.values()) {
                if (value.equals("plz")) {
                    preparedStatement.setInt(index++, Integer.parseInt(value));
                } else {
                    preparedStatement.setString(index++, value);
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("benutzername");
                    String name = resultSet.getString("name");
                    String lastName = resultSet.getString("nachname");
                    String adresse = resultSet.getString("strasse");
                    int plz = resultSet.getInt("plz");
                    String ort = resultSet.getString("ort");
                    String platform = resultSet.getString("gekauft_ueber");
                    int customer_id = resultSet.getInt("customer_id");

                    Customer customer = new Customer(username, name, lastName, adresse, plz, ort, platform, customer_id);
                    listOfCustomers.add(customer);
                }
                return listOfCustomers;
            } catch (SQLException e) {
                System.err.println("Fehler beim schließen des ResultSets: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Verbinden zur Datenbank. " + e.getMessage());
        }
        return listOfCustomers;
    }


    public List<Customer> searchCustomerLike(Map<String, String> filledFields) {
        List<Customer> listOfCustomers = new ArrayList<>();
        String sql = "SELECT * FROM customer WHERE ";

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (sql.contains("LIKE")) {
                //TODO:: das += gegen einen StringBuilder austauschen damit nicht immer neue Kopien aus dem Objekt
                // erzeugt werden!
                sql += " OR ";
            }

            sql += entry.getKey() + " LIKE '" + entry.getValue() + "%'";
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("benutzername");
                    String name = resultSet.getString("name");
                    String lastName = resultSet.getString("nachname");
                    String street = resultSet.getString("strasse");
                    int plz = resultSet.getInt("plz");
                    String ort = resultSet.getString("ort");
                    String platform = resultSet.getString("gekauft_ueber");
                    int customer_id = resultSet.getInt("customer_id");

                    Customer customer = new Customer(username, name, lastName, street, plz, ort, platform, customer_id);
                    listOfCustomers.add(customer);
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Verbinden zur Datenbank. " + e.getMessage());
        }
        return listOfCustomers;
    }


    public boolean deleteCustomer(int customerID) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerID);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Fehler beim Löschen des Kunden. " + e.getMessage());
            return false;
        }
    }


    public List<Category> getAllCategories() {
        List<Category> listOfCategories = new ArrayList<>();
        String sql = "SELECT * FROM category";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int id = resultSet.getInt("category_id");
                    listOfCategories.add(new Category(name, id));
                }
                return listOfCategories;
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim abrufen der Kategorien. " + e.getMessage());
        }
        return listOfCategories;
    }


    public List<Article> getAllArticles() {
        List<Article> listOfArticle = new ArrayList<>();
        String sql = "SELECT * FROM article";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int article_id = resultSet.getInt("article_id");
                    int category_id = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    String farbe = resultSet.getString("farbe");
                    double preis = resultSet.getDouble("kaufpreis");
                    LocalDate kaufdatum = resultSet.getDate("kaufdatum").toLocalDate();
                    String hersteller = resultSet.getString("hersteller");
                    String gekauft_bei = resultSet.getString("gekauft_ueber");
                    String verarbeitung = resultSet.getString("verarbeitung");
                    int menge = resultSet.getInt("menge");
                    int bestand = resultSet.getInt("bestand");
                    listOfArticle.add(new Article(article_id, category_id, name, farbe, preis, kaufdatum, hersteller, gekauft_bei, verarbeitung, menge, bestand));
                }
                return listOfArticle;
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim entnehmen der Artikel aus der Datenbank. " + e.getMessage());
        }
        return listOfArticle;
    }


    public boolean addArticle(Map<String, String> filledFields) {
        String sql = "INSERT INTO article (";
        StringBuilder whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append(entry.getKey());
        }
        whereClause.append(", bestand) VALUES (");
        sql += whereClause.toString();

        whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append("?");
        }
        whereClause.append(", ?)");
        sql += whereClause.toString();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;
            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                if (entry.getKey().equals("kaufpreis")) {
                    preparedStatement.setDouble(index++, Double.parseDouble(entry.getValue()));
                } else if (entry.getKey().equals("kaufdatum")) {
                    preparedStatement.setDate(index++, java.sql.Date.valueOf(LocalDate.parse(entry.getValue())));
                } else if (entry.getKey().equals("menge") || entry.getKey().equals("category_id")) {
                    preparedStatement.setInt(index++, Integer.parseInt(entry.getValue()));
                }
                else {
                    preparedStatement.setString(index++, entry.getValue());
                }
            }
            int bestand = Integer.parseInt(filledFields.get("menge"));
            preparedStatement.setInt(index, bestand);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Fehler beim Hinzufügen des Artikels. " + e.getMessage());
        }
        return false;
    }


    public List<Article> searchArticleLike(Map<String, String> filledFields) {
        List<Article> listOfFoundetArticles = new ArrayList<>();
        // SELECT name, farbe, kaufpreis FROM article WHERE name LIKE "%abc%" AND farbe LIKE "%Blau%" AND kaufpreis LIKE "%2.3%";
        String sql = "SELECT ";
        StringBuilder whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append(entry.getKey());
        }
        whereClause.append(" FROM article WHERE ");

        sql += whereClause.toString();
        whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(entry.getKey()).append(" LIKE \"%").append(entry.getValue()).append("%\"");
        }

        sql += whereClause.toString();



        return listOfFoundetArticles;
    }


    public boolean deleteArticle(int articleID) {
        String sql = "DELETE FROM article WHERE article_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, articleID);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Fehler beim Löschen des Artikels. " + e.getMessage());
            return false;
        }
    }


    public int getCustomerID(String username, String password) {
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


}