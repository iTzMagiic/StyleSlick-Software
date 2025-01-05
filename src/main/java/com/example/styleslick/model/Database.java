package com.example.styleslick.model;

import com.example.styleslick.service.RulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Database {

    private final String URL = "jdbc:mysql://localhost:3306/styleslickdb";
    private final String USER;
    private final String PASSWORD;
    /*
        Log-Level:
            info:   Zeigt allgemeine Informationen (z. B. Statusmeldungen)
            debug:  Detaillierte Debugging-Informationen (nur für Entwickler)
            warn:   Warnungen (z. B. mögliche Probleme, aber das Programm läuft weiter)
            error:  Schwere Fehler, die oft das Programm beeinflussen
     */
    private static final Logger logger = LoggerFactory.getLogger(Database.class);


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
            logger.error("Verbindung zur Datenbank fehlgeschlagen! " + e.getMessage(), e);
        }
        return false; // Verbindung fehlgeschlagen
    }


    public String getTotalSales() {
        String gesamt_einnahmen = "NULL";
        String sql = "SELECT SUM(amount - shipping_cost) AS gesamt_einnahmen FROM `order`";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    gesamt_einnahmen = String.valueOf(resultSet.getDouble("gesamt_einnahmen"));
                    gesamt_einnahmen = gesamt_einnahmen.replace(".", ",");
                    gesamt_einnahmen += "€";
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim abrufen der gesamt Einnahmen. " + e.getMessage());
        }
        return gesamt_einnahmen;
    }


    public String getTotalExpenditure() {
        String gesamt_ausgaben = "NULL";
        String sql = "SELECT SUM(kaufpreis * menge) AS gesamt_ausgaben FROM article";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    gesamt_ausgaben = String.valueOf(resultSet.getDouble("gesamt_ausgaben"));
                    gesamt_ausgaben = gesamt_ausgaben.replace(".", ",");
                    gesamt_ausgaben += "€";
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim abrufen der gesamt Ausgaben. " + e.getMessage());
        }
        return gesamt_ausgaben;
    }


    public String getTotalProfit() {
        String gewinn = "NULL";
        String sql = "SELECT (SELECT SUM(amount - shipping_cost) FROM `order`) - (SELECT SUM(kaufpreis * menge) FROM article) AS gewinn";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    gewinn = String.valueOf(resultSet.getDouble("gewinn"));
                    gewinn = gewinn.replace(".", ",");
                    gewinn += "€";
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim abrufen der gesamt gewinne. " + e.getMessage());
        }
        return gewinn;
    }


    public String getTotalCustomer() {
        String numberOfCustomers = "NULL";
        String sql = "SELECT COUNT(*) AS anzahl_kunden FROM customer";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    numberOfCustomers = String.valueOf(resultSet.getInt("anzahl_kunden"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim abrufen der gesamten anzahl der Kunden. " + e.getMessage());
        }
        return numberOfCustomers;
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
            int index = 1;

            for (Map.Entry<String, String> field : filledFields.entrySet()) {
                if (field.getKey().equals("postal_code")) {
                    preparedStatement.setInt(index++, Integer.parseInt(field.getValue()));
                } else {
                    preparedStatement.setString(index++, field.getValue());
                }
            }
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            RulesService.showErrorAlert("Fehler beim hinzufügen des Kunden.");
            System.out.println("Fehler beim Verbinden zur Datenbank. " + e.getMessage());
        }
    }


    public boolean isUsernameExist(String username) {
        String sql = "SELECT username FROM customer WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim abrufen ob der username schon existiert. " + e.getMessage());
            return false;
        }
        return false;
    }


    public List<Customer> getAllCustomers() {
        List<Customer> listOfCustomers = new ArrayList<>();
        String sql = "SELECT * FROM customer";


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String name = resultSet.getString("name");
                    String last_name = resultSet.getString("last_name");
                    String street = resultSet.getString("street");
                    int postal_code = resultSet.getInt("postal_code");
                    String city = resultSet.getString("city");
                    String purchased_from = resultSet.getString("purchased_from");
                    int customer_id = resultSet.getInt("customer_id");

                    Customer customer = new Customer(username, name, last_name, street, postal_code, city, purchased_from, customer_id);
                    listOfCustomers.add(customer);
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Abrufen der Kunden: " + e.getMessage());
        }
        return listOfCustomers;
    }


    public List<Customer> searchCustomer(Map<String, String> filledFields) {
        List<Customer> listOfCustomers = new ArrayList<>();
        String sql = "SELECT * FROM customer WHERE ";
        StringBuilder whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(entry.getKey()).append(" LIKE ?");
        }
        sql += whereClause.toString();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int index = 1;
            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                if (entry.getKey().equals("postal_code")) {
                    preparedStatement.setInt(index++, Integer.parseInt(entry.getValue()));
                } else {
                    preparedStatement.setString(index++, entry.getValue() + "%");
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String name = resultSet.getString("name");
                    String last_name = resultSet.getString("last_name");
                    String street = resultSet.getString("street");
                    int postal_code = resultSet.getInt("postal_code");
                    String city = resultSet.getString("city");
                    String purchased_from = resultSet.getString("purchased_from");
                    int customer_id = resultSet.getInt("customer_id");

                    Customer customer = new Customer(username, name, last_name, street, postal_code, city, purchased_from, customer_id);
                    listOfCustomers.add(customer);
                }
                return listOfCustomers;
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
            System.err.println("Fehler beim Löschen des Kunden. " + e.getMessage());
            return false;
        }
    }


    public boolean updateCustomer(Map<String, String> filledFields, int customerID) {
        String sql = "UPDATE customer SET ";
        StringBuilder setClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(entry.getKey()).append(" = ?");
        }
        setClause.append(" WHERE customer_id = ?");

        sql += setClause.toString();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;

            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                if (entry.getKey().equals("postal_code")) {
                    preparedStatement.setInt(index++, Integer.parseInt(entry.getValue()));
                } else {
                    preparedStatement.setString(index++, entry.getValue());
                }
            }

            preparedStatement.setInt(index, customerID);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Fehler beim bearbeiten des Kunden. " + e.getMessage());
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
            System.err.println("Fehler beim abrufen der Kategorien. " + e.getMessage());
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
                    String gekauft_bei = resultSet.getString("purchased_from");
                    String verarbeitung = resultSet.getString("verarbeitung");
                    int menge = resultSet.getInt("menge");
                    int bestand = resultSet.getInt("bestand");
                    listOfArticle.add(new Article(article_id, category_id, name, farbe, preis, kaufdatum, hersteller, gekauft_bei, verarbeitung, menge, bestand));
                }
                return listOfArticle;
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim entnehmen der Artikel aus der Datenbank. " + e.getMessage());
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

        if (!filledFields.containsKey("bestand")) {
            whereClause.append(", bestand) VALUES (");
            System.out.println("test kein bestand vorhanden ");
        } else {
            whereClause.append(") VALUES (");
            System.out.println("Bestand vorhanden " + filledFields.get("bestand"));
        }

        sql += whereClause.toString();
        whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append("?");
        }

        if (!filledFields.containsKey("bestand")) {
            whereClause.append(", ?)");
        } else {
            whereClause.append(")");
        }

        sql += whereClause.toString();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;
            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                if (entry.getKey().equals("kaufpreis")) {
                    preparedStatement.setDouble(index++, Double.parseDouble(entry.getValue()));
                } else if (entry.getKey().equals("kaufdatum")) {
                    preparedStatement.setDate(index++, java.sql.Date.valueOf(LocalDate.parse(entry.getValue())));
                } else if (entry.getKey().equals("menge") || entry.getKey().equals("category_id") || entry.getKey().equals("bestand")) {
                    preparedStatement.setInt(index++, Integer.parseInt(entry.getValue()));
                } else {
                    preparedStatement.setString(index++, entry.getValue());
                }
            }
            if (!filledFields.containsKey("bestand")) {
                int bestand = Integer.parseInt(filledFields.get("menge"));
                preparedStatement.setInt(index, bestand);
            }

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Fehler beim Hinzufügen des Artikels. " + e.getMessage());
        }
        return false;
    }


    public boolean updateArticle(Map<String, String> filledFields, int articleID) {
        String sql = "UPDATE article SET ";
        StringBuilder setClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(entry.getKey()).append(" = ?");
        }
        setClause.append(" WHERE article_id = ?");

        sql += setClause.toString();


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;

            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                if (entry.getKey().equals("kaufpreis")) {
                    preparedStatement.setDouble(index++, Double.parseDouble(entry.getValue()));
                } else if (entry.getKey().equals("kaufdatum")) {
                    preparedStatement.setDate(index++, java.sql.Date.valueOf(LocalDate.parse(entry.getValue())));
                } else if (entry.getKey().equals("menge") || entry.getKey().equals("category_id") || entry.getKey().equals("bestand")) {
                    preparedStatement.setInt(index++, Integer.parseInt(entry.getValue()));
                } else {
                    preparedStatement.setString(index++, entry.getValue());
                }
            }

            preparedStatement.setInt(index, articleID);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Fehler beim bearbeiten des Artikels. " + e.getMessage());
            return false;
        }
    }


    public List<Article> searchArticleLike(Map<String, String> filledFields) {
        List<Article> listOfFoundetArticles = new ArrayList<>();
        String sql = "SELECT * FROM article WHERE ";
        StringBuilder whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(entry.getKey()).append(" LIKE ?");
        }

        sql += whereClause.toString();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int index = 1;
            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                if (entry.getKey().equals("kaufpreis")) {
                    preparedStatement.setDouble(index++, Double.parseDouble(entry.getValue()));
                } else if (entry.getKey().equals("kaufdatum")) {
                    preparedStatement.setDate(index++, java.sql.Date.valueOf(LocalDate.parse(entry.getValue())));
                } else if (entry.getKey().equals("menge") || entry.getKey().equals("category_id") || entry.getKey().equals("bestand")) {
                    preparedStatement.setInt(index++, Integer.parseInt(entry.getValue()));
                } else {
                    preparedStatement.setString(index++, "%" + entry.getValue() + "%");
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int article_id = resultSet.getInt("article_id");
                    int category_id = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    String farbe = resultSet.getString("farbe");
                    double kaufpreis = resultSet.getDouble("kaufpreis");
                    LocalDate kaufdatum = resultSet.getDate("kaufdatum").toLocalDate();
                    String hersteller = resultSet.getString("hersteller");
                    String purchased_from = resultSet.getString("purchased_from");
                    String verarbeitung = resultSet.getString("verarbeitung");
                    int menge = resultSet.getInt("menge");
                    int bestand = resultSet.getInt("bestand");

                    listOfFoundetArticles.add(new Article(article_id, category_id, name, farbe, kaufpreis, kaufdatum, hersteller, purchased_from, verarbeitung, menge, bestand));
                }
                return listOfFoundetArticles;
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Suchen des Artikels. " + e.getMessage());
        }

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
            System.err.println("Fehler beim Löschen des Artikels. " + e.getMessage());
            return false;
        }
    }


}