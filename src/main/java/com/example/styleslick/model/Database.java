package com.example.styleslick.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



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
        logger.debug("\n\nSTART isConnected()");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            logger.info("ENDE isConnected() erfolgreich mit der Datenbank verbunden.");
            return true;
        } catch (SQLException e) {
            logger.error("ERROR isConnected() Verbindung zur Datenbank fehlgeschlagen! FEHLER: {} ", e.getMessage(), e);
        }
        return false;
    }


    public String getTotalSales() {
        logger.debug("\n\nSTART getTotalSales()");
        String totalSales = "0,00€";
        String sql = "SELECT SUM(payment_amount - shipping_cost) AS totalSales FROM invoice";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                totalSales = String.valueOf(resultSet.getDouble("totalSales"));
                totalSales = totalSales.replace(".", ",");
                totalSales += "€";

                logger.info("ENDE erfolgreich den Gesamtumsatz berechnet {}.", totalSales);
                return totalSales;
            } else {
                logger.warn("WARN getTotalSales() Es gibt keinen Gesamtumsatz. SQL Query: {}", sql);
            }

        } catch (SQLException e) {
            logger.error("ERROR getTotalSales() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return totalSales;
    }


    public String getTotalExpenditure() {
        logger.debug("\n\nSTART getTotalExpenditure()");
        String TotalExpenditure = "0,00€";
        String sql = "SELECT SUM(price * amount) AS TotalExpenditure FROM article";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    TotalExpenditure = String.valueOf(resultSet.getDouble("TotalExpenditure"));
                    TotalExpenditure = TotalExpenditure.replace(".", ",");
                    TotalExpenditure += "€";
                    if (resultSet.wasNull()) {
                        logger.info("ENDE getTotalExpenditure() erfolgreich Gesamtausgaben berechnet {}.", TotalExpenditure);

                    } else {
                        logger.warn("WARN getTotalExpenditure() Es gibt keine Gesamtausgaben. SQL Query: {}", sql);
                    }
                }
        } catch (SQLException e) {
            logger.error("ERROR getTotalExpenditure() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        return TotalExpenditure;
    }


    //TODO:: Ab hier Methode weiter verbessern!
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
            logger.error("ERROR getTotalProfit() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
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
            logger.error("ERROR getTotalCustomer() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
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
            logger.error("ERROR getAllCustomers() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return listOfCustomers;
    }


    public String getCustomerNumber(int customerID) {
        logger.debug("START getCustomerNumber().");
        String sql = "SELECT customer_number FROM customer WHERE customer_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("ENDE getCustomerNumber() erfolgreich Kunden-Nr aus der Datenbank exportiert.");
                    return resultSet.getString("customer_number");
                } else {
                    logger.warn("WARN getCustomerNumber() fehlgeschlagen, es wurde kein Kunde mit der Kunden ID gefunden.");
                    return "NULL";
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getCustomerNumber() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return "ERROR";
        }
    }


    public int getCustomerID(String customer_number) {
        logger.debug("START getCustomerID().");

        String sqlQuery = "SELECT customer_id FROM customer WHERE customer_number = ?";
        logger.debug("SQL Query: {}", sqlQuery);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, customer_number);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("ENDE getCustomerID erfolgreich.");
                    return resultSet.getInt("customer_id");
                }
            }

        } catch (SQLException e) {
            logger.error("ERROR getCustomerID() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        logger.warn("WARN getCustomerID() fehlgeschlagen es konnte kein Kunde mit der Kunden-Nr gefunden werden.");
        return -1;
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
            logger.error("ERROR isUsernameExist() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return false;
        }
        return false;
    }


    public boolean addCustomer(Map<String, String> filledFields) {
        logger.debug("\n\nSTART addCustomer() filledFields Länge: {}", filledFields.size());

        String sql = generateInsertIntoQueryWithNumber("customer", filledFields);
        logger.debug("Generated sql Query: {}", sql);


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int index = 1;

            for (Map.Entry<String, String> field : filledFields.entrySet()) {
                preparedStatement.setString(index++, field.getValue());
            }

            preparedStatement.setString(index, generateCustomerNumber());
            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE addCustomer() erfolgreich.");
                return true;
            } else {
                logger.warn("WARN addCustomer() es konnte kein neuer Kunde hinzugefügt werden.");
                return false;
            }

        } catch (SQLException e) {
            logger.error("ERROR addCustomer() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return false;
    }


    private String generateCustomerNumber() {
        logger.debug("START generateCustomerNumber()");

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
                    logger.info("ENDE generateCustomerNumber() erfolgreich. Erstellte Kundennummer: {}", customerNumber);
                    return customerNumber;
                } else {
                    logger.warn("WARN generateCustomerNumber() Keine vorhandene Kundennummer für das aktuelle Jahr.");
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR generateCustomerNumber() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return "ERROR";
    }


    public boolean updateCustomer(Map<String, String> filledFields, int customerID) {
        logger.debug("\n\nSTART updateCustomer() filledFields Länge: {}", filledFields.size());
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
            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE updateCustomer() Der Kunde wurde erfolgreich bearbeitet.");
                return true;
            } else {
                logger.warn("WARN updateCustomer() Der Kunde konnte nicht bearbeitet werden");
                return false;
            }

        } catch (SQLException e) {
            logger.error("ERROR updateCustomer() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return false;
    }


    public List<Customer> searchCustomerLike(Map<String, String> filledFields) {
        logger.debug("\n\nSTART searchCustomer() filledFields Länger: {}", filledFields.size());
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
                preparedStatement.setString(index++,  "%" + entry.getValue() + "%");
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
            logger.error("ERROR searchCustomer() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return listOfCustomers;
    }


    public boolean deleteCustomer(int customerID) {
        logger.debug("\n\nSTART deleteCustomer()");

        String sql = "DELETE FROM customer WHERE customer_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerID);
            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE deleteCustomer() Der Kunde wurde erfolgreich aus der Datenbank gelöscht.");
                return true;
            } else {
                logger.warn("WARN deleteCustomer() Der Kunde konnte nicht gelöscht werden.");
                return false;
            }

        } catch (SQLException e) {
            logger.error("ERROR deleteCustomer() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return false;
    }


    public List<Category> getAllCategories() {
        logger.info("START getAllCategories().");
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
                logger.info("ENDE erfolgreich getAllCategories().");
                return listOfCategories;
            }
        } catch (SQLException e) {
            logger.error("ERROR getAllCategories() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        logger.warn("WARN getAllCategories() fehler beim Laden der Kategorien.");
        return listOfCategories;
    }


    public String getCategoryName(int categoryID) {
        logger.debug("START getCategoryName().");
        String sql = "SELECT name FROM category WHERE category_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("ENDE getCategoryName() erfolgreich den Kategorie Namen aus der Datenbank exportiert.");
                    return resultSet.getString("name");
                } else {
                    logger.warn("WARN getCategoryName() fehlgeschlagen, es wurde kein Kategorie Name mit der ID gefunden.");
                    return "NULL";
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getCategoryName() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return "NULL";
        }
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
            logger.error("ERROR addCategory() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
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
            logger.error("ERROR updateCategory() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
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
            logger.error("ERROR deleteCategory() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
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
                    String category_name = getCategoryName(categoryID);
                    String name = resultSet.getString("name");
                    String color = resultSet.getString("color");
                    double price = resultSet.getDouble("price");
                    LocalDate purchase_date = resultSet.getDate("purchase_date").toLocalDate();
                    String manufacturer = resultSet.getString("manufacturer");
                    String purchased_from = resultSet.getString("purchased_from");
                    String quality = resultSet.getString("quality");
                    int amount = resultSet.getInt("amount");
                    int stock = resultSet.getInt("stock");


                    listOfArticle.add(new Article(articleID, categoryID, category_name, name, color, price, purchase_date, manufacturer, purchased_from, quality, amount, stock));
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getAllArticles() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
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
            logger.error("ERROR addArticle() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
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
            logger.error("ERROR updateArticle() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
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
                    String category_name = getCategoryName(categoryID);
                    String name = resultSet.getString("name");
                    String color = resultSet.getString("color");
                    double price = resultSet.getDouble("price");
                    LocalDate purchase_date = resultSet.getDate("purchase_date").toLocalDate();
                    String manufacturer = resultSet.getString("manufacturer");
                    String purchased_from = resultSet.getString("purchased_from");
                    String quality = resultSet.getString("quality");
                    int amount = resultSet.getInt("amount");
                    int stock = resultSet.getInt("stock");

                    listOfFoundetArticles.add(new Article(articleID, categoryID, category_name, name, color, price, purchase_date, manufacturer, purchased_from, quality, amount, stock));
                }
                return listOfFoundetArticles;
            }
        } catch (SQLException e) {
            logger.error("ERROR searchArticlesLike() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        return listOfFoundetArticles;
    }


    public int getStockOfArticle(int articleID) {
        logger.debug("START getStockOfArticle()");
        String sql = "SELECT stock FROM article WHERE article_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, articleID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("ENDE getStockOfArticle() erfolgreich den Bestand aus der Datenbank exportiert.");
                    return resultSet.getInt("stock");
                } else {
                    logger.warn("WARN getStockOfArticle() fehlgeschlagen, kein passender Artikel mit der ID gefunden.");
                    return -9999;
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getStockOfArticle() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return -9999;
        }
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
            logger.error("ERROR deleteArticle() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
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
                    String customer_number = getCustomerNumber(customerID);
                    String invoice_number = resultSet.getString("invoice_number");
                    LocalDate purchase_date = resultSet.getDate("purchase_date").toLocalDate();
                    String payment_method = resultSet.getString("payment_method");
                    String transaction_number = resultSet.getString("transaction_number");
                    double payment_amount = resultSet.getDouble("payment_amount");
                    double shipping_cost = resultSet.getDouble("shipping_cost");
                    String shipping_receipt = resultSet.getString("shipping_receipt");
                    String shipping_method = resultSet.getString("shipping_method");


                    listOfInvoices.add(new Invoice(customer_number, invoiceID, customerID, invoice_number, purchase_date, payment_method,
                            transaction_number, payment_amount, shipping_cost, shipping_receipt, shipping_method));
                }

                logger.info("ENDE getAllInvoices() erfolgreich. Alle Bestellungen wurden aus der Datenbank geladen.");
                return listOfInvoices;
            }
        } catch (SQLException e) {
            logger.error("ERROR getAllInvoices() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        logger.warn("WARN getAllInvoices() fehlgeschlagen. Bestellungen konnten nicht geladen werden.");
        return listOfInvoices;
    }


    public int addInvoice(Map<String, String> filledFields) {
        logger.debug("START addInvoice().");
        String sqlQuery = generateInsertIntoQueryWithNumber("invoice", filledFields);
        logger.debug("Generierter SQL Query: {}", sqlQuery);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            int index = 1;
            int customerID = getCustomerID(filledFields.get("customer"));

            for (Map.Entry<String, String> entry : filledFields.entrySet()) {

                if (entry.getKey().equals("purchase_date")) {
                    preparedStatement.setDate(index++, java.sql.Date.valueOf(LocalDate.parse(entry.getValue())));

                } else if (entry.getKey().equals("payment_amount") || entry.getKey().equals("shipping_cost")) {
                    preparedStatement.setDouble(index++, Double.parseDouble(entry.getValue()));

                } else if (entry.getKey().equals("customer_id")) {
                    preparedStatement.setInt(index++, getCustomerID(entry.getValue()));

                } else {
                    preparedStatement.setString(index++, entry.getValue());
                }
            }

            preparedStatement.setString(index, generateInvoiceNumber());

            preparedStatement.executeUpdate();
            logger.info("SQL Query wurde erfolgreich ausgeführt.");

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    int invoiceID = resultSet.getInt(1);
                    logger.info("ENDE addInvoice() erfolgreich. Bestellung wurde in die Datenbank importiert" +
                            " mit der ID: {}", invoiceID);
                    return invoiceID;
                }
            }

        } catch (SQLException e) {
            logger.error("ERROR addInvoice() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        logger.warn("WARN addInvoice() fehlgeschlagen. Bestellung konnte nicht erstellt werden.");
        return -1;
    }


    public boolean addItemToInvoice(int invoiceID, Map<String, String> filledFields) {
        logger.debug("START addItemToInvoice().");

        String sqlAddItemToInvoice = "INSERT INTO invoice_item (invoice_id, article_id, amount) VALUES (?, ?, ?)";
        String sqlUpdateArticleStock = "UPDATE article SET stock = stock - ? WHERE article_id = ?";

        logger.debug("sqlAddItemToInvoice Query: {}", sqlAddItemToInvoice);
        logger.debug("sqlUpdateArticleStock Query: {}", sqlAddItemToInvoice);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement addItemToInvoicePreparedStatement = connection.prepareStatement(sqlAddItemToInvoice);
                 PreparedStatement updatePreparedStatement = connection.prepareStatement(sqlUpdateArticleStock)) {


                addItemToInvoicePreparedStatement.setInt(1, invoiceID);
                addItemToInvoicePreparedStatement.setInt(2, Integer.parseInt(filledFields.get("article_id")));
                addItemToInvoicePreparedStatement.setInt(3, Integer.parseInt(filledFields.get("amount")));

                int addResult = addItemToInvoicePreparedStatement.executeUpdate();


                updatePreparedStatement.setInt(1, Integer.parseInt(filledFields.get("amount")));
                updatePreparedStatement.setInt(2, Integer.parseInt(filledFields.get("article_id")));

                int updateResult = updatePreparedStatement.executeUpdate();

                if (addResult == 1 && updateResult == 1) {
                    connection.commit();
                    logger.info("ENDE addItemToInvoice() erfolgreich Artikel zur Bestellung hinzugefügt sowie Bestand geändert.");
                    return true;
                } else {
                    connection.rollback();
                    logger.warn("WARN addItemToInvoice() fehlgeschlagen, Artikel konnte nicht der Bestellung " + invoiceID +
                            " hinzugefügt werden.");
                    return false;
                }

            } catch (SQLException e) {
                connection.rollback();
                logger.error("ERROR addItemToInvoice() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("ERROR addItemToInvoice() Verbindung fehlgeschlagen. FEHLER: {}", e.getMessage(), e);
        }

        return false;
    }


    public List<InvoiceItem> getInvoiceItems(int invoice_id) {
        logger.debug("START getInvoiceItems().");
        List<InvoiceItem> listOfInvoiceItems = new ArrayList<>();
        String sql = "SELECT i.*, a.name FROM invoice_item i INNER JOIN article a ON i.article_id = a.article_id WHERE " +
                "invoice_id = ?";

        logger.debug("SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, invoice_id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int articleID = resultSet.getInt("article_id");
                    int amount = resultSet.getInt("amount");
                    String articleName = resultSet.getString("name");

                    listOfInvoiceItems.add(new InvoiceItem(articleID, amount, articleName));
                }
                logger.debug("getInvoiceItems() listOfInvoiceItems größe: {}", listOfInvoiceItems.size());
                logger.info("ENDE getInvoiceItems erfolgreich.");
                return listOfInvoiceItems;
            }
        } catch (SQLException e) {
            logger.error("ERROR getInvoiceItems() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return listOfInvoiceItems;
        }
    }


    public int getInvoiceID(String invoice_number) {
        logger.debug("START getInvoiceID().");
        String sql = "SELECT invoice_id FROM invoice WHERE invoice_number = ?";

        logger.debug("SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, invoice_number);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("ENDE getInvoiceID() erfolgreich.");
                    return resultSet.getInt("invoice_id");
                } else {
                    logger.warn("WARN getInvoiceID() Es wurde keine Bestellung mit der invoice_number gefunden: {}", invoice_number);
                    return -1;
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getInvoiceID() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return -1;
        }
    }


    public boolean deleteInvoice(int invoiceID) {
        logger.debug("START deleteInvoice().");
        String getItemsSql = "SELECT article_id, amount FROM invoice_item WHERE invoice_id = ?";
        String updateStockSql = "UPDATE article SET stock = stock + ? WHERE article_id = ?";
        String deleteItemsSql = "DELETE FROM invoice_item WHERE invoice_id = ?";
        String deleteInvoiceSql = "DELETE FROM invoice WHERE invoice_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            connection.setAutoCommit(false);

            try (PreparedStatement getItemsStmt = connection.prepareStatement(getItemsSql);
                 PreparedStatement updateStockStmt = connection.prepareStatement(updateStockSql);
                 PreparedStatement deleteItemsStmt = connection.prepareStatement(deleteItemsSql);
                 PreparedStatement deleteInvoiceStmt = connection.prepareStatement(deleteInvoiceSql)) {

                getItemsStmt.setInt(1, invoiceID);
                ResultSet itemsResult = getItemsStmt.executeQuery();

                while (itemsResult.next()) {
                    int articleID = itemsResult.getInt("article_id");
                    int amount = itemsResult.getInt("amount");

                    updateStockStmt.setInt(1, amount);
                    updateStockStmt.setInt(2, articleID);
                    int updatedRows = updateStockStmt.executeUpdate();

                    if (updatedRows != 1) {
                        throw new SQLException("Fehler beim Aktualisieren des Bestands für article_id: " + articleID);
                    }
                }

                deleteItemsStmt.setInt(1, invoiceID);
                deleteItemsStmt.executeUpdate();

                deleteInvoiceStmt.setInt(1, invoiceID);
                int deletedRows = deleteInvoiceStmt.executeUpdate();

                if (deletedRows != 1) {
                    throw new SQLException("Fehler beim Löschen der Bestellung mit invoice_id: " + invoiceID);
                }

                connection.commit();
                logger.info("ENDE deleteInvoice() erfolgreich. Bestellung und Artikel wurden gelöscht, Bestand wurde aktualisiert.");
                return true;

            } catch (SQLException e) {
                connection.rollback();
                logger.error("ERROR deleteInvoice() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            logger.error("ERROR deleteInvoice() Verbindung fehlgeschlagen. FEHLER: {}", e.getMessage(), e);
        }

        return false;
    }


    private String generateInvoiceNumber() {
        logger.debug("START generateInvoiceNumber()");
        // SQL-Abfrage, um die höchste Rechnungsnummer für das aktuelle Jahr zu finden
        String sql = "SELECT IFNULL(MAX(CAST(SUBSTRING(invoice_number, 6) AS UNSIGNED)), 0) + 1 AS new_invoice_number " +
                "FROM invoice " +
                "WHERE SUBSTRING(invoice_number, 2, 4) = YEAR(CURDATE())";

        logger.debug("SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int new_invoice_number = resultSet.getInt("new_invoice_number");

                    // Generieren der Rechnungsnummer: 'I' + Jahr + Nummer mit führenden Nullen
                    String invoiceNumber = "I" + java.time.Year.now() + String.format("%04d", new_invoice_number);

                    logger.info("ENDE generateInvoiceNumber() erfolgreich. Erstellte Bestell-Nr: {}", invoiceNumber);
                    return invoiceNumber;
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR generateInvoiceNumber() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        logger.warn("WARN generateInvoiceNumber fehlgeschlagen. Fehler beim erstellen der Bestell-Nr.");
        return "ERROR";
    }

    private String generateInsertIntoQuery(String table, Map<String, String> filledFields) {
        String columnList = generateColumnList(filledFields);
        String placeHolderList = generatePlaceHolderList(filledFields.size());

        return "INSERT INTO " + table + " (" + columnList + ") VALUES (" + placeHolderList + ")";
    }

    private String generateInsertIntoQueryWithNumber(String table, Map<String, String> filledFields) {
        String columnList = generateColumnListWithNumber(table, filledFields);
        String placeHolderList = generatePlaceHolderList(filledFields.size() + 1);


        return "INSERT INTO " + table + " (" + columnList + ") VALUES (" + placeHolderList + ")";
    }

    private String generateColumnListWithNumber(String table, Map<String, String> filledFields) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            stringBuilder.append(entry.getKey());

            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.append(table).append("_number").toString();
    }

    private String generateColumnList(Map<String, String> filledFields) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(entry.getKey());
        }

        return stringBuilder.toString();
    }

    private String generatePlaceHolderList(int numberOfPlaceHolders) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < numberOfPlaceHolders; i++) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("?");
        }

        return stringBuilder.toString();
    }

}