package com.example.styleslick.controller;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Customer;
import com.example.styleslick.model.Invoice;
import com.example.styleslick.service.*;
import com.example.styleslick.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class InvoiceManagementController implements Initializable {

    private InvoiceService invoiceService;
    private ArticleService articleService;
    private CategoryService categoryService;
    private CustomerService customerService;

    @FXML
    private TableColumn<Article, Double> column_article_amount;

    @FXML
    private TableColumn<Article, Integer> column_article_articleID;

    @FXML
    private TableColumn<Article, Integer> column_article_categoryID;

    @FXML
    private TableColumn<Article, String> column_article_color;

    @FXML
    private TableColumn<Article, String> column_article_manufacturer;

    @FXML
    private TableColumn<Article, String> column_article_name;

    @FXML
    private TableColumn<Article, Double> column_article_price;

    @FXML
    private TableColumn<Article, LocalDate> column_article_purchase_date;

    @FXML
    private TableColumn<Article, String> column_article_purchased_from;

    @FXML
    private TableColumn<Article, String> column_article_quality;

    @FXML
    private TableColumn<Article, Integer> column_article_stock;

    @FXML
    private TableColumn<Customer, String> column_customer_city;

    @FXML
    private TableColumn<Customer, String> column_customer_country;

    @FXML
    private TableColumn<Customer, String> column_customer_first_name;

    @FXML
    private TableColumn<Customer, String> column_customer_last_name;

    @FXML
    private TableColumn<Customer, String> column_customer_number;

    @FXML
    private TableColumn<Customer, String> column_customer_postal_code;

    @FXML
    private TableColumn<Customer, String> column_customer_purchased_from;

    @FXML
    private TableColumn<Customer, String> column_customer_street;

    @FXML
    private TableColumn<Customer, String> column_customer_username;


    @FXML
    private TableColumn<Invoice, Integer> column_invoice_customerID;

    @FXML
    private TableColumn<Invoice, String> column_invoice_number;

    @FXML
    private TableColumn<Invoice, Double> column_invoice_payment_amount;

    @FXML
    private TableColumn<Invoice, String> column_invoice_payment_method;

    @FXML
    private TableColumn<Invoice, LocalDate> column_invoice_purchase_date;

    @FXML
    private TableColumn<Invoice, Double> column_invoice_shipping_cost;

    @FXML
    private TableColumn<Invoice, String> column_invoice_shipping_method;

    @FXML
    private TableColumn<Invoice, String> column_invoice_shipping_receipt;

    @FXML
    private TableColumn<Invoice, String> column_invoice_transaction_number;

    @FXML
    private DatePicker datePicker_purchase_date;

    @FXML
    private TextField field_amount;

    @FXML
    private TextField field_articleID;

    @FXML
    private TextField field_customerID;

    @FXML
    private TextField field_payment_amount;

    @FXML
    private TextField field_payment_method;

    @FXML
    private TextField field_shipping_cost;

    @FXML
    private TextField field_shipping_method;

    @FXML
    private TextField field_shipping_receipt;

    @FXML
    private TextField field_transaction_number;

    @FXML
    private TableView<Article> tableView_articles;

    @FXML
    private TableView<Customer> tableView_customers;

    @FXML
    private TableView<Invoice> tableView_invoices;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceService = InvoiceService.getInstance();
        articleService = ArticleService.getInstance();
        categoryService = CategoryService.getInstance();
        customerService = CustomerService.getInstance();

        tableView_invoices.getSelectionModel().selectedItemProperty();

        executeShowAllInvoices();
    }


    private void executeShowAllCustomers() {
        tableView_customers.setVisible(true);
        tableView_invoices.setVisible(false);
        tableView_articles.setVisible(false);

        column_customer_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        column_customer_first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        column_customer_last_name.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_customer_street.setCellValueFactory(new PropertyValueFactory<>("street"));
        column_customer_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        column_customer_postal_code.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        column_customer_purchased_from.setCellValueFactory(new PropertyValueFactory<>("purchasedFrom"));
        column_customer_number.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));
        column_customer_country.setCellValueFactory(new PropertyValueFactory<>("country"));

        ObservableList<Customer> observableList = FXCollections.observableArrayList(customerService.getCustomers());
        tableView_customers.setItems(observableList);
    }


    private void executeShowAllArticles() {
        tableView_articles.setVisible(true);
        tableView_customers.setVisible(false);
        tableView_invoices.setVisible(false);


        column_article_articleID.setCellValueFactory(new PropertyValueFactory<>("articleID"));
        column_article_categoryID.setCellValueFactory(new PropertyValueFactory<>("categoryID"));
        column_article_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_article_color.setCellValueFactory(new PropertyValueFactory<>("color"));
        column_article_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        column_article_purchase_date.setCellValueFactory(new PropertyValueFactory<>("purchase_date"));
        column_article_manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        column_article_purchased_from.setCellValueFactory(new PropertyValueFactory<>("purchased_from"));
        column_article_quality.setCellValueFactory(new PropertyValueFactory<>("quality"));
        column_article_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        column_article_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        ObservableList<Article> observableList = FXCollections.observableArrayList(articleService.getAllArticles());
        tableView_articles.setItems(observableList);
    }


    private void executeShowAllInvoices() {
        tableView_invoices.setVisible(true);
        tableView_articles.setVisible(false);
        tableView_customers.setVisible(false);

        column_invoice_number.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        column_invoice_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        column_invoice_purchase_date.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        column_invoice_payment_method.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        column_invoice_transaction_number.setCellValueFactory(new PropertyValueFactory<>("transactionNumber"));
        column_invoice_payment_amount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        column_invoice_shipping_method.setCellValueFactory(new PropertyValueFactory<>("shippingMethod"));
        column_invoice_shipping_receipt.setCellValueFactory(new PropertyValueFactory<>("shippingReceipt"));
        column_invoice_shipping_cost.setCellValueFactory(new PropertyValueFactory<>("shippingCost"));

        ObservableList<Invoice> observableList = FXCollections.observableArrayList(invoiceService.getAllInvoices());
        tableView_invoices.setItems(observableList);
    }


    private void executeAddInvoice() {
        Map<String, String> invoiceFields = new HashMap<>();
        Map<String, String> itemFields = new HashMap<>();


        if (datePicker_purchase_date.getValue() == null) {
            AlertService.showErrorAlert("Bestelldatum darf nicht leer sein.");
            return;
        }

        invoiceFields.put("customer_id", field_customerID.getText());
        invoiceFields.put("purchase_date", datePicker_purchase_date.getValue().toString());
        invoiceFields.put("payment_method", field_payment_method.getText());
        invoiceFields.put("transaction_number", field_transaction_number.getText());
        invoiceFields.put("payment_amount", field_payment_amount.getText());
        invoiceFields.put("shipping_method", field_shipping_method.getText());
        invoiceFields.put("shipping_receipt", field_shipping_receipt.getText());
        invoiceFields.put("shipping_cost", field_shipping_cost.getText());

        itemFields.put("article_id", field_articleID.getText());
        itemFields.put("amount", field_amount.getText());



        if (!invoiceService.addInvoice(invoiceFields, itemFields)) {
            return;
        }

        field_customerID.clear();
        datePicker_purchase_date.setValue(null);
        field_payment_method.clear();
        field_transaction_number.clear();
        field_payment_amount.clear();
        field_shipping_method.clear();
        field_shipping_receipt.clear();
        field_shipping_cost.clear();
    }


    private void executeExitInvoiceManagement() {
        articleService.clearSession();
        customerService.clearSession();
        invoiceService.clearSession();
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
    }


    @FXML
    private void onKeyPressedEnterShowAllInvoices(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAllInvoices();
        }
    }

    @FXML
    private void onKeyPressedEnterShowAllCustomers(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAllCustomers();
        }
    }

    @FXML
    private void onKeyPressedEnterShowAllArticles(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAllArticles();
        }
    }

    @FXML
    private void onKeyPressedEnterAddInvoice(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeAddInvoice();
        }
    }

    @FXML
    private void onKeyPressedEnterExitInvoiceManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitInvoiceManagement();
        }
    }




    @FXML
    private void onMouseClickedShowAllInvoices(MouseEvent event) {
        executeShowAllInvoices();
    }

    @FXML
    private void onMouseClickedShowAllCustomers(MouseEvent event) {
        executeShowAllCustomers();
    }

    @FXML
    private void onMouseClickedShowAllArticles(MouseEvent event) {
        executeShowAllArticles();
    }

    @FXML
    private void onMouseClickedAddInvoice(MouseEvent event) {
        executeAddInvoice();
    }

    @FXML
    private void onMouseClickedExitInvoiceManagement(MouseEvent event) {
        executeExitInvoiceManagement();
    }


}
