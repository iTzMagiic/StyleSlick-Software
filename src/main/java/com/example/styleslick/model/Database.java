package com.example.styleslick.model;

import com.example.styleslick.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO:: Methoden müssen noch geloggt werden !!

public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private final String URL = "jdbc:mysql://localhost:3306/styleslickdb";
    private final String USER;
    private final String PASSWORD;


    public Database(String USER, String PASSWORD) {
        this.USER = USER;
        this.PASSWORD = PASSWORD;
    }


    public boolean isConnected() {
        logger.debug("START isConnected()");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (connection != null) {
                logger.info("ENDE isConnected() erfolgreich.");
                return true;
            } else {
                logger.warn("Keine Verbindung zur Datenbank.");
            }
        } catch (SQLException e) {
            logger.error("ERROR Verbindung zur Datenbank fehlgeschlagen! FEHLER: {} ", e.getMessage(), e);
        }
        return false;
    }


    public String getTotalSales() {
        logger.debug("\n\nSTART getTotalSales()");
        String totalSales = "0,00€";
        String sql = "SELECT SUM(payment_amount - shipping_cost) AS totalSales FROM invoice";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    totalSales = String.valueOf(resultSet.getDouble("totalSales"));
                    totalSales = totalSales.replace(".", ",");
                    totalSales += "€";
                    logger.info("ENDE erfolgreich den Gesamtumsatz berechnet.");
                    return totalSales;
                } else {
                    logger.warn("WARN getTotalSales() Es gibt keinen Gesamtumsatz. SQL Query: {}", sql);
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getTotalSales() fehler beim abrufen der gesamteinnahmen. FEHLER: {}", e.getMessage(), e);
        }
        return totalSales;
    }


    public String getTotalExpenditure() {
        logger.debug("START getTotalExpenditure()");
        String TotalExpenditure = "0,00€";
        String sql = "SELECT SUM(price * amount) AS TotalExpenditure FROM article";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    TotalExpenditure = String.valueOf(resultSet.getDouble("TotalExpenditure"));
                    TotalExpenditure = TotalExpenditure.replace(".", ",");
                    TotalExpenditure += "€";
                    logger.info("ENDE getTotalExpenditure() erfolgreich.");
                    return TotalExpenditure;
                } else {
                    logger.warn("WARN getTotalSales() Es gibt keine Gesamtausgaben. SQL Query: {}", sql);
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getTotalExpenditure() fehlgeschlagen beim abrufen der gesamtausgaben. FEHLER: {}", e.getMessage(), e);
        }
        return TotalExpenditure;
    }


    public String getTotalProfit() {
        logger.debug("START getTotalProfit()");
        String TotalProfit = "0,00€";
        String sql = "SELECT (SELECT SUM(payment_amount - shipping_cost) FROM invoice) - (SELECT SUM(price * amount) FROM article) AS TotalProfit";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    TotalProfit = String.valueOf(resultSet.getDouble("TotalProfit"));
                    TotalProfit = TotalProfit.replace(".", ",");
                    TotalProfit += "€";
                    logger.info("ENDE getTotalProfit() erfolgreich.");
                    return TotalProfit;
                } else {
                    logger.warn("WARN getTotalProfit() Es gibt keinen Gewinn. SQL Query: {}", sql);
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR fehlgeschlagen beim abrufen des Gewinns. FEHLER: {}", e.getMessage(), e);
        }
        return TotalProfit;
    }


    public String getTotalCustomer() {
        String numberOfCustomers = "0";
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


    public List<Customer> getAllCustomers() {
        List<Customer> listOfCustomers = new ArrayList<>();
        String sql = "SELECT * FROM customer";


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");
                    String street = resultSet.getString("street");
                    String postal_code = resultSet.getString("postal_code");
                    String city = resultSet.getString("city");
                    String country = resultSet.getString("country");
                    String purchased_from = resultSet.getString("purchased_from");
                    int customerID = resultSet.getInt("customer_id");
                    String customer_number = resultSet.getString("customer_number");

                    Customer customer = new Customer(username, first_name, last_name, street, postal_code, city, country, purchased_from, customerID, customer_number);
                    listOfCustomers.add(customer);
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Abrufen der Kunden: " + e.getMessage());
        }
        return listOfCustomers;
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


    public boolean addCustomer(Map<String, String> filledFields) {
        logger.debug("START addCustomer() Parameter Länge: {}", filledFields.size());
        String sql = "INSERT INTO customer (";
        StringBuilder whereClause = new StringBuilder();

        // WHERE-Bedingung aus der Map filledFields einfügen und mit(" AND " &" = ?")trennen;
        for (Map.Entry<String, String> key : filledFields.entrySet()) {

            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append(key.getKey());
        }
        whereClause.append(", customer_number) VALUES (");
        sql += whereClause.toString();

        whereClause = new StringBuilder();

        for (Map.Entry<String, String> values : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append(" ?");
        }

        whereClause.append(", ?)");
        sql += whereClause.toString();

        logger.debug("DEBUG addCustomer() SQL Query: {}", sql);


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;

            for (Map.Entry<String, String> field : filledFields.entrySet()) {
                preparedStatement.setString(index++, field.getValue());
            }
            preparedStatement.setString(index, createCustomerNumber());
            preparedStatement.executeUpdate();
            logger.info("ENDE addCustomer() erfolgreich.");
            return true;

        } catch (SQLException e) {
            AlertService.showErrorAlert("Fehler beim hinzufügen des Kunden.");
            logger.error("ERROR addCustomer() Kunde konnte nicht in die Datenbank geschrieben werden. FEHLER: {}", e.getMessage(), e);
        }
        return false;
    }


    private String createCustomerNumber() {
        logger.debug("START createCustomerNumber()");
        // SQL-Abfrage, um die höchste Kundennummer für das aktuelle Jahr zu finden
        String sql = "SELECT IFNULL(MAX(CAST(SUBSTRING(customer_number, 6) AS UNSIGNED)), 0) + 1 AS new_customer_number " +
                "FROM customer " +
                "WHERE SUBSTRING(customer_number, 2, 4) = YEAR(CURDATE())";


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int new_customer_number = resultSet.getInt("new_customer_number");

                    // Generieren der Kundennummer: 'C' + Jahr + Nummer mit führenden Nullen
                    String customerNumber = "C" + java.time.Year.now() + String.format("%04d", new_customer_number);
                    logger.info("ENDE createCustomerNumber() erfolgreich. Erstellte Kundennummer: {}", customerNumber);
                    return customerNumber;
                } else {
                    logger.warn("WARN createCustomerNumber() Keine vorhandene Kundennummer für das aktuelle Jahr.");
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR createCustomerNumber() Erstellen der neuen Kundennummer fehlgeschlagen. {}", e.getMessage(), e);
        }
        return "ERROR";
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
                preparedStatement.setString(index++, entry.getValue());
            }

            preparedStatement.setInt(index, customerID);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Fehler beim bearbeiten des Kunden. " + e.getMessage());
            return false;
        }
    }


    public List<Customer> searchCustomer(Map<String, String> filledFields) {
        logger.debug("START searchCustomer() Parameter Länger: {}", filledFields.size());
        List<Customer> listOfCustomers = new ArrayList<>();
        StringBuilder whereClause = new StringBuilder();

        String sql = "SELECT * FROM customer WHERE ";


        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(entry.getKey()).append(" LIKE ?");
        }
        sql += whereClause.toString();
        logger.debug("DEBUG searchCustomer() SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int index = 1;
            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                preparedStatement.setString(index++, entry.getValue() + "%");
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");
                    String street = resultSet.getString("street");
                    String postal_code = resultSet.getString("postal_code");
                    String city = resultSet.getString("city");
                    String country = resultSet.getString("country");
                    String purchased_from = resultSet.getString("purchased_from");
                    int customerID = resultSet.getInt("customer_id");
                    String customer_number = resultSet.getString("customer_number");

                    Customer customer = new Customer(username, first_name, last_name, street, postal_code, city, country, purchased_from, customerID, customer_number);
                    listOfCustomers.add(customer);
                }
                logger.info("ENDE searchCustomer() erfolgreich. Länger der Liste von Kunden: {}", listOfCustomers.size());
                return listOfCustomers;
            }
        } catch (SQLException e) {
            logger.error("ERROR searchCustomer() Fehler beim abrufen der Kunden aus der Datenbank. FEHLER: {}", e.getMessage(), e);
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


    public List<Category> getAllCategories() {
        logger.info("START getAllCategories.");
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
            logger.error("Fehlber beim abrufen der Kategorien. FEHELER: {}", e.getMessage(), e);
        }
        return listOfCategories;
    }


    public boolean addCategory(Map<String, String> filledFields) {
        logger.info("Methode addCategory START.");
        String sql = "INSERT INTO category (";
        StringBuilder whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append(entry.getKey());
        }
        whereClause.append(") VALUES (");
        sql += whereClause.toString();

        whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append("?");
        }
        whereClause.append(")");
        sql += whereClause.toString();

        logger.debug("SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;
            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                preparedStatement.setString(index++, entry.getValue());
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error("ERROR beim hinzufügen der Kategorie. FEHLER: {}", e.getMessage(), e);
            return false;
        }
    }


    public boolean updateCategory(Map<String, String> filledFields, int categoryID) {
        logger.info("START updateCategory().");
        String sql = "UPDATE category SET ";
        StringBuilder setClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(entry.getKey()).append(" = ?");
        }
        setClause.append(" WHERE category_id = ?");
        sql += setClause.toString();

        logger.debug("SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;
            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                preparedStatement.setString(index++, entry.getValue());
            }
            preparedStatement.setInt(index, categoryID);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error("ERROR beim bearbeiten der Kategorie. FEHLER: {}", e.getMessage(), e);
            return false;
        }
    }


    public boolean deleteCategory(int categoryID) {
        logger.info("START deleteCategory().");
        String sql = "DELETE FROM category WHERE category_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryID);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error("ERROR beim löschen der Kategorie. FEHLER: {}", e.getMessage(), e);
            return false;
        }
    }


    public List<Article> getAllArticles() {
        List<Article> listOfArticle = new ArrayList<>();
        String sql = "SELECT * FROM article";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int articleID = resultSet.getInt("article_id");
                    int categoryID = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    String color = resultSet.getString("color");
                    double price = resultSet.getDouble("price");
                    LocalDate purchase_date = resultSet.getDate("purchase_date").toLocalDate();
                    String manufacturer = resultSet.getString("manufacturer");
                    String purchased_from = resultSet.getString("purchased_from");
                    String quality = resultSet.getString("quality");
                    int amount = resultSet.getInt("amount");
                    int stock = resultSet.getInt("stock");
                    listOfArticle.add(new Article(articleID, categoryID, name, color, price, purchase_date, manufacturer, purchased_from, quality, amount, stock));
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim entnehmen der Artikel aus der Datenbank. " + e.getMessage());
            return listOfArticle;
        }
        return listOfArticle;
    }


    public boolean addArticle(Map<String, String> filledFields) {
        logger.debug("\n\nSTART addArticle()");
        String sql = "INSERT INTO article (";
        StringBuilder whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append(entry.getKey());
        }

        if (!filledFields.containsKey("stock")) {
            whereClause.append(", stock) VALUES (");
        } else {
            whereClause.append(") VALUES (");
            System.out.println("stock vorhanden " + filledFields.get("stock"));
        }

        sql += whereClause.toString();
        whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append("?");
        }

        if (!filledFields.containsKey("stock")) {
            whereClause.append(", ?)");
        } else {
            whereClause.append(")");
        }

        sql += whereClause.toString();
        logger.debug("SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;
            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                if (entry.getKey().equals("price")) {
                    preparedStatement.setDouble(index++, Double.parseDouble(entry.getValue()));
                } else if (entry.getKey().equals("purchase_date")) {
                    preparedStatement.setDate(index++, java.sql.Date.valueOf(LocalDate.parse(entry.getValue())));
                } else if (entry.getKey().equals("amount") || entry.getKey().equals("category_id") || entry.getKey().equals("stock")) {
                    preparedStatement.setInt(index++, Integer.parseInt(entry.getValue()));
                } else {
                    preparedStatement.setString(index++, entry.getValue());
                }
            }
            if (!filledFields.containsKey("stock")) {
                int stock = Integer.parseInt(filledFields.get("amount"));
                preparedStatement.setInt(index, stock);
            }

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("ERROR addArticle() Artikel konnte nicht in die Datenbank importiert werden! FEHLER: {}", e.getMessage(), e);
            return false;
        }

        logger.info("ENDE addArticle() Artikel wurde erfolgreich in die Datenbank importiert.");
        return true;
    }


    public boolean updateArticle(Map<String, String> filledFields, int articleID) {
        logger.debug("\n\nSTART updateArticle()");
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
        logger.debug("SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;

            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                if (entry.getKey().equals("price")) {
                    preparedStatement.setDouble(index++, Double.parseDouble(entry.getValue()));
                } else if (entry.getKey().equals("purchase_date")) {
                    preparedStatement.setDate(index++, java.sql.Date.valueOf(LocalDate.parse(entry.getValue())));
                } else if (entry.getKey().equals("amount") || entry.getKey().equals("category_id") || entry.getKey().equals("stock")) {
                    preparedStatement.setInt(index++, Integer.parseInt(entry.getValue()));
                } else {
                    preparedStatement.setString(index++, entry.getValue());
                }
            }
            preparedStatement.setInt(index, articleID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("ERROR updateArticle() Fehler beim bearbeiten der Artikel. FEHLER: {}", e.getMessage(), e);
            return false;
        }

        logger.info("ENDE updateArticle() Der Artikel wurde erfolgreich in der Datenbank bearbeitet.");
        return true;
    }


    public List<Article> searchArticlesLike(Map<String, String> filledFields) {
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
                if (entry.getKey().equals("price")) {
                    preparedStatement.setDouble(index++, Double.parseDouble(entry.getValue()));
                } else if (entry.getKey().equals("purchase_date")) {
                    preparedStatement.setDate(index++, java.sql.Date.valueOf(LocalDate.parse(entry.getValue())));
                } else if (entry.getKey().equals("amount") || entry.getKey().equals("category_id") || entry.getKey().equals("stock")) {
                    preparedStatement.setInt(index++, Integer.parseInt(entry.getValue()));
                } else {
                    preparedStatement.setString(index++, "%" + entry.getValue() + "%");
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int articleID = resultSet.getInt("article_id");
                    int categoryID = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    String color = resultSet.getString("color");
                    double price = resultSet.getDouble("price");
                    LocalDate purchase_date = resultSet.getDate("purchase_date").toLocalDate();
                    String manufacturer = resultSet.getString("manufacturer");
                    String purchased_from = resultSet.getString("purchased_from");
                    String quality = resultSet.getString("quality");
                    int amount = resultSet.getInt("amount");
                    int stock = resultSet.getInt("stock");

                    listOfFoundetArticles.add(new Article(articleID, categoryID, name, color, price, purchase_date, manufacturer, purchased_from, quality, amount, stock));
                }
                return listOfFoundetArticles;
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Suchen des Artikels. " + e.getMessage());
        }

        return listOfFoundetArticles;
    }


    public boolean deleteArticle(int articleID) {
        logger.debug("START deleteArticle().");
        String sql = "DELETE FROM article WHERE article_id = ?";
        logger.debug("SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, articleID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("ERROR deleteArticle() Fehler beim löschen des Artikels aus der Datenbank. FEHLER: {}", e.getMessage(), e);
            return false;
        }

        logger.info("ENDE deleteArticle() Der Artikel wurde erfolgreich aus der Datenbank gelöscht.");
        return true;
    }


    public List<Invoice> getAllInvoices() {
        logger.debug("START getAllInvoices()");
        String sql = "SELECT * FROM invoice";
        List<Invoice> listOfInvoices = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int invoiceID = resultSet.getInt("invoice_id");
                    int customerID = resultSet.getInt("customer_id");
                    String invoice_number = resultSet.getString("invoice_number");
                    LocalDate purchase_date = resultSet.getDate("purchase_date").toLocalDate();
                    String payment_method = resultSet.getString("payment_method");
                    String transaction_number = resultSet.getString("transaction_number");
                    double payment_amount = resultSet.getDouble("payment_amount");
                    double shipping_cost = resultSet.getDouble("shipping_cost");
                    String shipping_receipt = resultSet.getString("shipping_receipt");
                    String shipping_method = resultSet.getString("shipping_method");

                    listOfInvoices.add(new Invoice(invoiceID, customerID, invoice_number, purchase_date, payment_method,
                            transaction_number, payment_amount, shipping_cost, shipping_receipt, shipping_method));
                }
                logger.info("ENDE erfolgreich alle Rechnungen geladen.");
                return listOfInvoices;
            }
        } catch (SQLException e) {
            logger.error("ERROR Fehler beim exportieren der Rechnungen. FEHLER: {}", e.getMessage(), e);
        }
        return listOfInvoices;
    }


    public boolean addInvoice(Map<String, String> filledFields) {
        String sqlQuery = generateInsertIntoQuery("invoice", filledFields);

        return false;
    }


    private String createInvoiceNumber() {
        logger.debug("START createInvoiceNumber()");
        // SQL-Abfrage, um die höchste Rechnungsnummer für das aktuelle Jahr zu finden
        String sql = "SELECT IFNULL(MAX(CAST(SUBSTRING(invoice_number, 6) AS UNSIGNED)), 0) + 1 AS new_invoice_number " +
                "FROM invoice " +
                "WHERE SUBSTRING(invoice_number, 2, 4) = YEAR(CURDATE())";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int new_invoice_number = resultSet.getInt("new_invoice_number");

                    // Generieren der Rechnungsnummer: 'I' + Jahr + Nummer mit führenden Nullen
                    String invoiceNumber = "I" + java.time.Year.now() + String.format("%04d", new_invoice_number);
                    logger.info("ENDE createInvoiceNumber() erfolgreich. Erstellte Rechnungsnummer: {}", invoiceNumber);
                    return invoiceNumber;
                } else {
                    logger.warn("WARN createInvoiceNumber() Keine vorhandene Rechnungsnummer für das aktuelle Jahr.");
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR createInvoiceNumber() Erstellen der neuen Rechnungsnummer fehlgeschlagen. {}", e.getMessage(), e);
        }
        return "ERROR";
    }



    private String generateInsertIntoQuery(String table, Map<String, String> filledFields) {
        StringBuilder stringBuilder = new StringBuilder();
        String sqlQuery = "INSERT INTO " + table + " (";

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(entry.getKey());
        }
        stringBuilder.append(") VALUES (");
        sqlQuery += stringBuilder.toString();

        stringBuilder.setLength(0);

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("?");
        }
        stringBuilder.append(")");
        sqlQuery += stringBuilder.toString();

        return sqlQuery;
    }

}