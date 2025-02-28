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

                logger.info("ENDE getTotalSales() erfolgreich den Gesamtumsatz berechnet {}.", totalSales);
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


    public String getTotalProfit() {
        logger.debug("\n\nSTART getTotalProfit()");
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
        logger.debug("\n\nSTART getTotalCustomer()");

        String numberOfCustomers = "0";
        String sql = "SELECT COUNT(*) AS anzahl_kunden FROM customer";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    numberOfCustomers = String.valueOf(resultSet.getInt("anzahl_kunden"));
                    logger.info("ENDE getTotalCustomer() erfolgreich die Anzahl der Kunden ermittelt.");
                } else {
                    logger.warn("WARN getTotalCustomer() es konnte nicht die Anzahl der Kunden ermittelt werden. SQL Query: {}", sql);
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getTotalCustomer() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return numberOfCustomers;
    }


    public List<Customer> getAllCustomers() {
        logger.debug("\n\nSTART getAllCustomers()");
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

        logger.info("ENDE getAllCustomers() listOfCustomers Länge: {}", listOfCustomers.size());
        return listOfCustomers;
    }


    public String getCustomerNumber(int customerID) {
        logger.debug("\n\nSTART getCustomerNumber().");

        String sql = "SELECT customer_number FROM customer WHERE customer_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    logger.info("ENDE getCustomerNumber() erfolgreich Kunden-Nr aus der Datenbank exportiert.");
                    return resultSet.getString("customer_number");
                } else {
                    logger.warn("WARN getCustomerNumber() fehlgeschlagen, es wurde kein Kunde mit der customer_id gefunden.");
                    return "NULL";
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getCustomerNumber() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return "ERROR";
        }
    }


    public int getCustomerID(String customer_number) {
        logger.debug("\n\nSTART getCustomerID().");

        String sqlQuery = "SELECT customer_id FROM customer WHERE customer_number = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, customer_number);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info("ENDE getCustomerID() erfolgreich.");
                    return resultSet.getInt("customer_id");
                } else {
                    logger.warn("WARN getCustomerID() fehlgeschlagen es konnte kein Kunde mit der Kunden-Nr gefunden werden.");
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getCustomerID() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return -1;
    }


    public boolean isUsernameExist(String username) {
        logger.debug("\n\nSTART isUsernameExist()");

        String sql = "SELECT EXISTS (SELECT 1 FROM customer WHERE username = ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    logger.info("ENDE isUsernameExist() erfolgreich");
                    return resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            logger.error("ERROR isUsernameExist() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        logger.warn("WARN isUsernameExist() - Fehler aufgetreten oder kein Ergebnis erhalten. Rückgabe: true");
        return true;
    }


    public boolean isCustomerNumberExist(String customerNumber) {
        logger.debug("\n\nSTART isCustomerNumberExist()");

        String sql = "SELECT EXISTS (SELECT 1 FROM customer WHERE customer_number = ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, customerNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("ENDE isCustomerNumberExists() erfolgreich");
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR isCustomerNumberExist() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        // Die Methode gibt True zurück, um das Anlegen eines Kunden zu verhindern, falls die DB-Abfrage fehlschlägt.
        logger.warn("WARN isCustomerNumberExist() - Fehler aufgetreten oder kein Ergebnis erhalten. Rückgabe: true");
        return true;
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
        logger.debug("\n\nSTART generateCustomerNumber()");

        // SQL-Abfrage, um die höchste Kundennummer für das aktuelle Jahr zu finden
        String sql = "SELECT IFNULL(MAX(CAST(SUBSTRING(customer_number, 6) AS UNSIGNED)), 0) + 1 AS new_customer_number " +
                "FROM customer " +
                "WHERE SUBSTRING(customer_number, 2, 4) = YEAR(CURDATE())";

        logger.debug("sql: {}", sql);


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

        logger.debug("sql: {}", sql);

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

        logger.debug("SQL Query: {}", sql);


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int index = 1;

            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                preparedStatement.setString(index++, "%" + entry.getValue() + "%");
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
                logger.info("ENDE searchCustomer() Länger der Liste von Kunden: {}", listOfCustomers.size());
                return listOfCustomers;
            }
        } catch (SQLException e) {
            logger.error("ERROR searchCustomer() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return listOfCustomers;
    }


    public boolean customerHasInvoices(int customerID) {
        logger.debug("\n\nSTART customerHasINvoices()");

        String sql = "SELECT EXISTS (SELECT 1 FROM invoice WHERE customer_id = ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    boolean invoiceExists = resultSet.getBoolean(1);

                    if (invoiceExists) {
                        logger.info("ENDE customerHasInvoices() Kunde hat Bestellungen.");
                    } else {
                        logger.warn("WARN customerHasInvoices() Kunde hat keine Bestellungen");
                    }

                    return invoiceExists;
                }

            }
        } catch (SQLException e) {
            logger.error("ERROR customerHasInvoices() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        return false;
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
        logger.debug("\n\nSTART getAllCategories().");
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
            logger.error("ERROR getAllCategories() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        logger.info("ENDE getAllCategories(). listOfCategories Länge: {}", listOfCategories.size());
        return listOfCategories;
    }


    public String getCategoryName(int categoryID) {
        logger.debug("\n\nSTART getCategoryName().");
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
        logger.debug("\n\nSTART addCategory()");

        String sql = generateInsertIntoQuery("category", filledFields);
        logger.debug("Generated sql Query: {}", sql);


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int index = 1;

            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                preparedStatement.setString(index++, entry.getValue());
            }

            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE addCategory() Die Kategorie wurde erfolgreich hinzugefügt.");
                return true;
            } else {
                logger.warn("WARN addCategory() Die Kategorie wurde nicht hinzugefügt.");
                return false;
            }

        } catch (SQLException e) {
            logger.error("ERROR addCategory() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return false;
    }


    public boolean isCategoryNameExist(String categoryName) {
        logger.debug("\n\nStart isCategoryNameExist()");

        String sql = "SELECT EXISTS (SELECT 1 FROM category WHERE name = ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, categoryName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("ENDE isCategoryNameExists() erfolgreich");
                    return resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            logger.error("ERROR isCategoryNameExist() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        logger.warn("WARN isCategoryNameExist() - Fehler aufgetreten oder kein Ergebnis erhalten. Rückgabe: true");
        return true;
    }


    public boolean updateCategory(Map<String, String> filledFields, int categoryID) {
        /*
        Diese Methode ist so flexibel aufgebaut, dass sie sich automatisch anpasst,
        wenn die Tabelle "category" in der Datenbank um zusätzliche Attribute (Spalten) erweitert wird.
        Dadurch sind nur minimale Änderungen am Code erforderlich.
         */

        logger.debug("\n\nSTART updateCategory().");

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
            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE updateCategory() Die Kategorie wurde erfolgreich bearbeitet.");
                return true;
            } else {
                logger.warn("WARN updateCategory() Die Kategorie konnte nicht bearbeitet werden.");
                return false;
            }

        } catch (SQLException e) {
            logger.error("ERROR updateCategory() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return false;
    }


    public List<Category> searchCategory(String name) {
        List<Category> foundetCategories = new ArrayList<>();

        String sql = "SELECT * FROM category WHERE name LIKE ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + name + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int categoryID = resultSet.getInt("category_id");
                    String categoryName = resultSet.getString("name");

                    foundetCategories.add(new Category(categoryName, categoryID));
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR searchCategory() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        return foundetCategories;
    }


    public boolean deleteCategory(int categoryID) {
        logger.info("\n\nSTART deleteCategory().");
        String sql = "DELETE FROM category WHERE category_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, categoryID);
            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE deleteCategory() Die Kategorie wurde erfolgreich gelöscht.");
                return true;
            } else {
                logger.warn("WARN deleteCategory() Die Kategorie konnte nicht gelöscht werden. category_id: {}", categoryID);
            }
        } catch (SQLException e) {
            logger.error("ERROR deleteCategory() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return false;
    }


    public boolean hasCategoryDependencies(int categoryID) {
        logger.debug("\n\nSTART hasCategoryDependencies()");

        String sql = "SELECT category_id FROM article WHERE category_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, categoryID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    logger.info("ENDE hasCategoryDependencies() Es gibt keine Abhängigkeiten zu der Kategorie.");
                    return false;
                } else {
                    logger.info("ENDE hasCategoryDependencies() Es gibt noch Artikel mit Abhängigkeiten zur Kategorie.");
                    return true;
                }
            }

        } catch (SQLException e) {
            logger.error("ERROR hasCategoryDependencies() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return true;
        }
    }


    public List<Article> getAllArticles() {
        logger.debug("\n\nSTART getAllArticles()");

        List<Article> listOfArticle = new ArrayList<>();
        String sql = "SELECT * FROM article";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int articleID = resultSet.getInt("article_id");
                    int categoryID = resultSet.getInt("category_id");
                    String articleNumber = resultSet.getString("article_number");
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

                    listOfArticle.add(new Article(articleID, categoryID, articleNumber, category_name, name, color, price, purchase_date, manufacturer, purchased_from, quality, amount, stock));
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getAllArticles() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return listOfArticle;
        }
        logger.info("ENDE getAllArticles() listOfArticle Länge: {}", listOfArticle.size());
        return listOfArticle;
    }


    public List<Article> getAvailableArticles() {
        logger.debug("\n\nSTART getAvailableArticles()");

        List<Article> listOfArticle = new ArrayList<>();
        String sql = "SELECT * FROM article WHERE stock > 0";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int articleID = resultSet.getInt("article_id");
                    int categoryID = resultSet.getInt("category_id");
                    String articleNumber = resultSet.getString("article_number");
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

                    listOfArticle.add(new Article(articleID, categoryID, articleNumber, category_name, name, color, price, purchase_date, manufacturer, purchased_from, quality, amount, stock));
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR getAvailableArticles() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return listOfArticle;
        }
        logger.info("ENDE getAvailableArticles() listOfArticle Länge: {}", listOfArticle.size());
        return listOfArticle;
    }


    public boolean addArticle(Map<String, String> filledFields) {
        logger.debug("\n\nSTART addArticle()");

        // Die Methode so lassen, da sie sich anpasst, ob nur die Menge oder auch der Bestand angegeben ist

        String sql = "INSERT INTO article (";
        StringBuilder whereClause = new StringBuilder();


        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append(entry.getKey());
        }

        whereClause.append(", article_number");

        if (!filledFields.containsKey("stock")) {
            whereClause.append(", stock) VALUES (");
        } else {
            whereClause.append(") VALUES (");
        }

        sql += whereClause.toString();
        whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(", ");
            }
            whereClause.append("?");
        }
        whereClause.append(", ?");

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

            preparedStatement.setString(index++, generateArticleNumber());

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


    public boolean hasArticleDependencies(int articleID) {
        logger.debug("\n\nSTART hasArticleDependencies()");

        String sql = "SELECT EXISTS (SELECT 1 FROM invoice_item WHERE article_id = ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, articleID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("ENDE hasArticleDependencies() erfolgreich");
                    return resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            logger.error("ERROR hasArticleDependencies() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        logger.warn("WARN hasArticleDependencies() - Fehler aufgetreten oder kein Ergebnis erhalten. Rückgabe: true");
        return true;
    }


    public boolean isArticleNumberExist(String articleNumber) {
        logger.debug("\n\nSTART isArticleNumberExist()");

        String sql = "SELECT EXISTS (SELECT 1 FROM article WHERE article_number = ?)";


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, articleNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("ENDE isArticleNumberExist() erfolgreich");
                    return resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            logger.error("ERROR isArticleNumberExist() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        logger.warn("WARN isArticleNumberExist() - Fehler aufgetreten oder kein Ergebnis erhalten. Rückgabe: true");
        return true;
    }


    private String generateArticleNumber() {
        logger.debug("\n\nSTART generateArticleNumber()");

        // SQL-Abfrage, um die höchste Artikelnummer für das aktuelle Jahr zu finden
        String sql = "SELECT IFNULL(MAX(CAST(SUBSTRING(article_number, 6) AS UNSIGNED)), 0) + 1 AS new_article_number " +
                "FROM article " +
                "WHERE SUBSTRING(article_number, 2, 4) = YEAR(CURDATE())";

        logger.debug("sql: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int new_article_number = resultSet.getInt("new_article_number");

                    // Generieren der Artikelnummer: 'A' + Jahr + Nummer mit führenden Nullen
                    String articleNumber = "A" + java.time.Year.now() + String.format("%04d", new_article_number);
                    logger.info("ENDE generateArticleNumber() erfolgreich. Erstellte Artikelnummer: {}", articleNumber);
                    return articleNumber;
                } else {
                    logger.warn("WARN generateArticleNumber() Keine vorhandene Artikelnummer für das aktuelle Jahr.");
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR generateArticleNumber() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        return "ERROR";
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
            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE updateArticle() Der Artikel wurde erfolgreich in der Datenbank bearbeitet.");
                return true;
            } else {
                logger.warn("WARN updateArticle() Der Artikel konnte nicht bearbeitet werden. Artikel-Nr: {}", articleID);
                return false;
            }

        } catch (SQLException e) {
            logger.error("ERROR updateArticle() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return false;
        }
    }


    public List<Article> searchArticlesLike(Map<String, String> filledFields) {
        logger.debug("\n\n Start searchArticlesLike()");

        List<Article> listOfFoundetArticles = new ArrayList<>();
        String sql = "SELECT * FROM article WHERE ";
        StringBuilder whereClause = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            if (entry.getKey().contains("price")) {
                whereClause.append(entry.getKey()).append(" = ?");
            } else {
                whereClause.append(entry.getKey()).append(" LIKE ?");
            }
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
                    String articleNumber = resultSet.getString("article_number");
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

                    listOfFoundetArticles.add(new Article(articleID, categoryID, articleNumber, category_name, name, color, price, purchase_date, manufacturer, purchased_from, quality, amount, stock));
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR searchArticlesLike() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        logger.info("ENDE searchArticlesLike() listOfFoundetArticles Länge: {}", listOfFoundetArticles.size());
        return listOfFoundetArticles;
    }


    public int getStockOfArticle(String articleNumber) {
        logger.debug("\n\nSTART getStockOfArticle()");

        String sql = "SELECT stock FROM article WHERE article_number = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, articleNumber);

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
        logger.debug("\n\nSTART deleteArticle().");

        String sql = "DELETE FROM article WHERE article_id = ?";
        logger.debug("SQL Query: {}", sql);


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, articleID);

            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE deleteArticle() Der Artikel wurde erfolgreich aus der Datenbank gelöscht.");
                return true;
            } else {
                logger.warn("WARN deleteArticle() Fehlgeschlagen kein passender Artikel mit der article_id gefunden.");
                return false;
            }

        } catch (SQLException e) {
            logger.error("ERROR deleteArticle() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return false;
        }
    }


    public List<Invoice> getAllInvoices() {
        logger.debug("\n\nSTART getAllInvoices()");

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

                logger.info("ENDE getAllInvoices() Alle Bestellungen wurden aus der Datenbank geladen. listOfInvoices Länge: {}", listOfInvoices.size());
                return listOfInvoices;
            }
        } catch (SQLException e) {
            logger.error("ERROR getAllInvoices() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return listOfInvoices;
        }
    }


    public boolean isInvoiceNumberExist(String invoiceNumber) {
        logger.debug("\n\nSTART isInvoiceNumberExist()");

        String sql = "SELECT EXISTS(SELECT 1 FROM invoice WHERE invoice_number = ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, invoiceNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.wasNull()) {
                        logger.warn("WARN isInvoiceNumberExist() keine bestellung mit der Bestell-Nr gefunden.");
                        return false;
                    }
                    logger.info("ENDE isInvoiceNumberExist() erfolgreich");
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("ERROR isInvoiceNumberExist() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }

        return false;
    }


    public boolean updateInvoice(Map<String, String> filledFields, Invoice invoice) {
        logger.debug("\n\nSTART updateInvoice()");

        String sql = "UPDATE invoice SET ";
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : filledFields.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(entry.getKey()).append(" = ?");
        }

        stringBuilder.append(" WHERE invoice_id = ?");
        sql += stringBuilder.toString();

        logger.debug("SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int index = 1;

            for (Map.Entry<String, String> entry : filledFields.entrySet()) {
                if (entry.getKey().equals("payment_amount") || entry.getKey().equals("shipping_cost") || entry.getKey().equals("shipping_method")) {
                    preparedStatement.setDouble(index++, Double.parseDouble(entry.getValue()));
                    continue;
                }
                if (entry.getKey().equals("purchase_date")) {
                    preparedStatement.setDate(index++, java.sql.Date.valueOf(entry.getValue()));
                    continue;
                }
                preparedStatement.setString(index++, entry.getValue());
            }

            preparedStatement.setInt(index, invoice.getInvoiceID());
            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE updateInvoice() Die Bestellung wurde erfolgreich bearbeitet.");
                return true;
            } else {
                logger.warn("WARN updateInvoice() Die Bestellung wurde nicht bearbeitet");
                return false;
            }

        } catch (SQLException e) {
            logger.error("ERROR updateCustomer() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
        }
        return false;
    }


    public boolean addInvoice(Map<String, String> invoiceFields) {
        logger.debug("\n\nSTART addInvoice()");


        String sql = generateInsertIntoQueryWithNumber("invoice", invoiceFields).replace("customer_number", "customer_id");
        logger.debug("sql: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement addInvoiceStatement = connection.prepareStatement(sql)) {

            int index = 1;

            for (Map.Entry<String, String> entry : invoiceFields.entrySet()) {

                if (entry.getKey().equals("purchase_date")) {
                    addInvoiceStatement.setDate(index++, java.sql.Date.valueOf(LocalDate.parse(entry.getValue())));

                } else if (entry.getKey().equals("payment_amount") || entry.getKey().equals("shipping_cost")) {
                    addInvoiceStatement.setDouble(index++, Double.parseDouble(entry.getValue()));

                } else if (entry.getKey().equals("customer_number")) {
                    addInvoiceStatement.setInt(index++, getCustomerID(entry.getValue()));

                } else {
                    addInvoiceStatement.setString(index++, entry.getValue());
                }
            }

            addInvoiceStatement.setString(index, generateInvoiceNumber());

            int resultAddInvoice = addInvoiceStatement.executeUpdate();

            if (resultAddInvoice != 1) {
                logger.error("ERROR addInvoice() Die Bestellung konnte nicht erstellt werden.");
                return false;
            }

            logger.info("ENDE addInvoice() Bestellung konnte erfolgreich angelegt werden.");
            return true;
        } catch (SQLException e) {
            logger.error("ERROR addItemToInvoice() Verbindung fehlgeschlagen. FEHLER: {}", e.getMessage(), e);
        }

        return false;
    }


    public boolean addItemToInvoice(int invoiceID, Map<String, String> filledFields) {
        logger.debug("\n\nSTART addItemToInvoice().");

        String sqlAddItemToInvoice = "INSERT INTO invoice_item (invoice_id, article_id, amount) VALUES (?, (SELECT article_id FROM article WHERE article_number = ?), ?)";
        String sqlUpdateArticleStock = "UPDATE article SET stock = stock - ? WHERE article_number = ?";


        logger.debug("sqlAddItemToInvoice Query: {}", sqlAddItemToInvoice);
        logger.debug("sqlUpdateArticleStock Query: {}", sqlAddItemToInvoice);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement addItemToInvoicePreparedStatement = connection.prepareStatement(sqlAddItemToInvoice);
                 PreparedStatement updatePreparedStatement = connection.prepareStatement(sqlUpdateArticleStock)) {


                addItemToInvoicePreparedStatement.setInt(1, invoiceID);
                addItemToInvoicePreparedStatement.setString(2, filledFields.get("article_number"));
                addItemToInvoicePreparedStatement.setInt(3, Integer.parseInt(filledFields.get("amount")));

                int addResult = addItemToInvoicePreparedStatement.executeUpdate();


                updatePreparedStatement.setInt(1, Integer.parseInt(filledFields.get("amount")));
                updatePreparedStatement.setString(2, filledFields.get("article_number"));

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


    public boolean reduceStockAndUpdateInvoiceItem(int articleID, int difference, int newAmount, int invoiceItemID) {
        logger.debug("\n\nSTART reduceStockAndUpdateInvoiceItem().");

        String sqlDecreaseArticle = "UPDATE article SET stock = stock - ? WHERE article_id = ?";
        String sqlUpdateInvoiceItem = "UPDATE invoice_item SET amount = ? WHERE invoice_item_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatementArticle = connection.prepareStatement(sqlDecreaseArticle);
             PreparedStatement preparedStatementInvoiceItem = connection.prepareStatement(sqlUpdateInvoiceItem)) {

            connection.setAutoCommit(false);

            // Artikel-Bestand reduzieren
            preparedStatementArticle.setInt(1, difference);
            preparedStatementArticle.setInt(2, articleID);
            int articleResult = preparedStatementArticle.executeUpdate();

            if (articleResult == 0) {
                logger.warn("ENDE reduceStockAndUpdateInvoiceItem() articleResult -> fehlgeschlagen.");
                connection.rollback();
                return false;
            }

            // InvoiceItem aktualisieren
            preparedStatementInvoiceItem.setInt(1, newAmount);
            preparedStatementInvoiceItem.setInt(2, invoiceItemID);
            int invoiceItemResult = preparedStatementInvoiceItem.executeUpdate();

            if (invoiceItemResult == 0) {
                logger.warn("ENDE reduceStockAndUpdateInvoiceItem() invoiceItemResult -> fehlgeschlagen.");
                connection.rollback();
                return false;
            }

            connection.commit();

            logger.info("ENDE reduceStockAndUpdateInvoiceItem() Bestände wurden erfolgreich aktualisiert.");
            return true;

        } catch (SQLException e) {
            logger.error("ERROR reduceStockAndUpdateInvoiceItem() Fehler bei der Datenbankoperation: {}", e.getMessage(), e);
        }

        return false;
    }

    public boolean increaseStockAndUpdateInvoiceItem(int articleID, int difference, int newAmount, int invoiceItemID) {
        logger.debug("\n\nSTART increaseStockAndUpdateInvoiceItem().");

        String sqlDecreaseArticle = "UPDATE article SET stock = stock + ? WHERE article_id = ?";
        String sqlUpdateInvoiceItem = "UPDATE invoice_item SET amount = ? WHERE invoice_item_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatementArticle = connection.prepareStatement(sqlDecreaseArticle);
             PreparedStatement preparedStatementInvoiceItem = connection.prepareStatement(sqlUpdateInvoiceItem)) {

            connection.setAutoCommit(false);

            // Artikel-Bestand reduzieren
            preparedStatementArticle.setInt(1, difference);
            preparedStatementArticle.setInt(2, articleID);
            int articleResult = preparedStatementArticle.executeUpdate();

            if (articleResult == 0) {
                logger.warn("ENDE increaseStockAndUpdateInvoiceItem() articleResult -> fehlgeschlagen.");
                connection.rollback();
                return false;
            }

            // InvoiceItem aktualisieren
            preparedStatementInvoiceItem.setInt(1, newAmount);
            preparedStatementInvoiceItem.setInt(2, invoiceItemID);
            int invoiceItemResult = preparedStatementInvoiceItem.executeUpdate();

            if (invoiceItemResult == 0) {
                logger.warn("ENDE increaseStockAndUpdateInvoiceItem() invoiceItemResult -> fehlgeschlagen.");
                connection.rollback();
                return false;
            }

            connection.commit();

            logger.info("ENDE increaseStockAndUpdateInvoiceItem() Bestände wurden erfolgreich aktualisiert.");
            return true;

        } catch (SQLException e) {
            logger.error("ERROR increaseStockAndUpdateInvoiceItem() Fehler bei der Datenbankoperation: {}", e.getMessage(), e);
        }

        return false;
    }


    public boolean deleteArticleFromInvoice(int invoice_item_id) {
        logger.debug("START deleteArticleFromInvoice()");

        String sql = "DELETE FROM invoice_item WHERE invoice_item_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, invoice_item_id);

            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE deleteArticleFromInvoice() Artikel wurde erfolgreich aus der Bestellung gelöscht.");
                return true;
            } else {
                logger.warn("WARN deleteArticleFromInvoice() Artikel konnte nicht aus der Bestellung gelöscht werden.");
                return false;
            }

        } catch (SQLException e) {
            logger.error("ERROR deleteArticleFromInvoice() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return false;
        }
    }


    public boolean addBackDeletedItem(int articleID, int amount) {
        logger.debug("\n\nSTART addBackDeletedItem()");

        String sql = "UPDATE article SET stock = stock + ? WHERE article_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, articleID);

            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                logger.info("ENDE addBackDeletedItem() erfolgreich der Bestand wurde erfolgreich wieder angepasst.");
                return true;
            } else {
                logger.warn("WARN addBackDeletedItem() fehlgeschlagen der Bestand des Artikels konnte nicht angepasst werden.");
                return false;
            }

        } catch (SQLException e) {
            logger.error("ERROR addBackDeletedItem() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return false;
        }
    }


    public List<InvoiceItem> getInvoiceItems(int invoice_id) {
        logger.debug("\n\nSTART getInvoiceItems().");

        List<InvoiceItem> listOfInvoiceItems = new ArrayList<>();
        String sql = "SELECT i.*, a.name, a.article_number FROM invoice_item i INNER JOIN article a ON i.article_id = a.article_id WHERE " +
                "invoice_id = ?";

        logger.debug("SQL Query: {}", sql);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, invoice_id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int articleID = resultSet.getInt("article_id");
                    String articleNumber = resultSet.getString("article_number");
                    int amount = resultSet.getInt("amount");
                    String articleName = resultSet.getString("name");
                    int invoice_item_id = resultSet.getInt("invoice_item_id");

                    listOfInvoiceItems.add(new InvoiceItem(articleID, articleNumber, amount, articleName, invoice_item_id));
                }

                logger.info("ENDE getInvoiceItems() listOfInvoiceItem Länge: {}", listOfInvoiceItems.size());
                return listOfInvoiceItems;
            }
        } catch (SQLException e) {
            logger.error("ERROR getInvoiceItems() Ein SQL-Fehler ist aufgetreten. FEHLER: {}", e.getMessage(), e);
            return listOfInvoiceItems;
        }
    }


    public int getInvoiceID(String invoice_number) {
        logger.debug("\n\nSTART getInvoiceID().");
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
        logger.debug("\n\nSTART deleteInvoice().");
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
        logger.debug("\n\nSTART generateInvoiceNumber()");
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