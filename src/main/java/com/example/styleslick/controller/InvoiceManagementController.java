package com.example.styleslick.controller;

import com.example.styleslick.model.Article;
import com.example.styleslick.model.Customer;
import com.example.styleslick.model.Invoice;
import com.example.styleslick.model.InvoiceItem;
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
import java.util.*;

public class InvoiceManagementController implements Initializable {

    private InvoiceService invoiceService;
    private ArticleService articleService;
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
    private TableColumn<Invoice, Integer> column_invoice_customerNumber;

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
    private TableColumn<InvoiceItem, Integer> column_invoice_item_articleID;

    @FXML
    private TableColumn<InvoiceItem, Integer> column_invoice_item_amount;

    @FXML
    private TableColumn<InvoiceItem, Integer> column_invoice_item_articleName;

    @FXML
    private DatePicker datePicker_purchase_date;

    @FXML
    private TextField field_amount;

    @FXML
    private TextField field_invoice_number;

    @FXML
    private TextField field_articleID;

    @FXML
    private TextField field_customer_number;

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

    @FXML
    private TableView<InvoiceItem> tableView_invoice_item;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceService = InvoiceService.getInstance();
        articleService = ArticleService.getInstance();
        customerService = CustomerService.getInstance();

        executeShowAllInvoices();
    }


    private void executeShowAllCustomers() {
        setInvoiceItemFieldsEditable(true);
        setTableViewVisible("customers");
        tableView_invoices.getSelectionModel().clearSelection();

        tableView_customers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                field_customer_number.setText(tableView_customers.getSelectionModel().getSelectedItem().getCustomerNumber());
            }
        });


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

        tableView_customers.getSelectionModel().clearSelection();
    }


    private void executeShowAllArticles() {
        setInvoiceItemFieldsEditable(true);
        setTableViewVisible("articles");
        tableView_invoices.getSelectionModel().clearSelection();

        tableView_articles.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                field_articleID.setText(String.valueOf(tableView_articles.getSelectionModel().getSelectedItem().getArticleID()));
            }
        });


        column_article_articleID.setCellValueFactory(new PropertyValueFactory<>("articleID"));
        column_article_categoryID.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
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

        tableView_articles.getSelectionModel().clearSelection();
    }


    private void executeShowAllInvoices() {
        setInvoiceItemFieldsEditable(true);
        setTableViewVisible("invoices");

        tableView_invoices.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                executeShowInvoiceItems();
            }
        });

        column_invoice_number.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        column_invoice_customerNumber.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));
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


    private void executeShowInvoiceItems() {

        //TODO:: Hier gefällt mir nicht das nur nach Selektierter Zelle geschaut wird aber was ist wenn ich eine Bestell-Nr
        //  Selbst eingetragen hab? dann muss ich ja auch die Bestellung einsehen können.


        Invoice selectedInvoice = tableView_invoices.getSelectionModel().getSelectedItem();

        if (selectedInvoice == null) {
            AlertService.showErrorAlert("Bitte wählen Sie eine Bestellung aus.");
            return;
        }

        tableView_invoices.getSelectionModel().clearSelection();

        invoiceService.setCurrentInvoice(selectedInvoice);

        SceneManager.switchScene("/com/example/styleslick/invoiceItem-View.fxml", "Bestellte Artikel", true);
    }


    private void executeAddInvoice() {
        Map<String, String> invoiceFields = new HashMap<>();
        Map<String, String> itemFields = new HashMap<>();


        if (datePicker_purchase_date.getValue() == null) {
            AlertService.showErrorAlert("Bestelldatum darf nicht leer sein.");
            return;
        }

        invoiceFields.put("customer_id", field_customer_number.getText());
        invoiceFields.put("purchase_date", datePicker_purchase_date.getValue().toString());
        invoiceFields.put("payment_method", field_payment_method.getText());
        invoiceFields.put("transaction_number", field_transaction_number.getText());
        invoiceFields.put("payment_amount", field_payment_amount.getText());
        invoiceFields.put("shipping_method", field_shipping_method.getText());
        invoiceFields.put("shipping_receipt", field_shipping_receipt.getText());
        invoiceFields.put("shipping_cost", field_shipping_cost.getText());



        if (!invoiceService.addInvoice(invoiceFields)) {
            return;
        }

        clearFields();
        executeShowAllInvoices();
    }


    private void executeAddItemToInvoice() {

        if (field_invoice_number.getLength() == 0 && tableView_invoices.getSelectionModel().getSelectedItem() == null) {
            AlertService.showErrorAlert("Bitte wählen Sie eine Bestellung aus oder tragen " +
                    "Sie eine Rechnung-Nr ein.");
            return;
        }

        Map<String, String> articleToAddToInvoice = new HashMap<>();


        articleToAddToInvoice.put("article_id", field_articleID.getText());
        articleToAddToInvoice.put("amount", field_amount.getText());


        if (tableView_invoices.getSelectionModel().getSelectedItem() != null) {
            if (invoiceService.addItemToInvoiceWithInvoiceID(articleToAddToInvoice, tableView_invoices.getSelectionModel().getSelectedItem().getInvoiceID())) {
                field_amount.clear();
                field_articleID.clear();
                field_invoice_number.clear();
                tableView_invoices.getSelectionModel().clearSelection();
                executeShowAllInvoices();
            }
        } else {
            if (invoiceService.addItemToInvoiceWithInvoiceNumber(articleToAddToInvoice, field_invoice_number.getText())) {
                field_amount.clear();
                field_articleID.clear();
                field_invoice_number.clear();
                executeShowAllInvoices();
            }
        }
    }


    private void executeDeleteInvoice() {
        if (tableView_invoices.getSelectionModel().getSelectedItem() == null) {
            AlertService.showErrorAlert("Bitte wählen Sie eine Bestellung aus, die Sie löschen möchten");
            executeShowAllInvoices();
            return;
        }

        Invoice selectedInvoice = tableView_invoices.getSelectionModel().getSelectedItem();

        //TODO:: Nachfragen ob der Bestand angepasst werde soll.
        if (AlertService.showConfirmAlertResult("Möchten Sie wirklich die Bestellung: '" + selectedInvoice.getInvoiceNumber() + "' löschen?")) {
            if (invoiceService.deleteInvoice(selectedInvoice.getInvoiceID())) {
                executeShowAllInvoices();
            }
        }
    }


    private void executeDeleteArticleFromInvoice() {

        List<InvoiceItem> listOfInvoiceItems;

        if (tableView_invoice_item.getSelectionModel().getSelectedItem() == null) {
            AlertService.showErrorAlert("Bitte wählen Sie ein Artikel aus einer Bestellung aus.");
            return;
        }

        InvoiceItem selectedArticle = tableView_invoice_item.getSelectionModel().getSelectedItem();
        tableView_invoice_item.getSelectionModel().clearSelection();

        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich den Artikel: '" + selectedArticle.getArticleName() +
                "' aus der Bestellung löschen?")) {
            return;
        }


        if (invoiceService.deleteArticleFromInvoice(selectedArticle.getInvoiceItemID())) {


            if (AlertService.showConfirmAlertResult("Soll der Bestand wieder angepasst werden?")) {
                invoiceService.addBackDeletedItem(selectedArticle.getArticleID(), selectedArticle.getAmount());
            }

            listOfInvoiceItems = invoiceService.getInvoiceItems(field_invoice_number.getText());

            if (listOfInvoiceItems.isEmpty()) {
                field_invoice_number.clear();
                executeShowAllInvoices();
                return;
            }

            column_invoice_item_articleID.setCellValueFactory(new PropertyValueFactory<>("articleID"));
            column_invoice_item_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            column_invoice_item_articleName.setCellValueFactory(new PropertyValueFactory<>("articleName"));


            ObservableList<InvoiceItem> observableList = FXCollections.observableArrayList(listOfInvoiceItems);
            tableView_invoice_item.setItems(observableList);
        }
    }


    private void executeExitInvoiceManagement() {
        articleService.clearSession();
        customerService.clearSession();
        invoiceService.clearSession();
        SceneManager.switchScene("/com/example/styleslick/Home-view.fxml", "Willkommen", false);
    }


    @FXML
    private void onKeyPressedEnterDeleteArticleFromInvoice(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeDeleteArticleFromInvoice();
        }
    }

    @FXML
    private void onKeyPressedEnterDeleteInvoice(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeDeleteInvoice();
        }
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
    private void onKeyPressedEnterShowInvoiceItems(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowInvoiceItems();
        }
    }

    @FXML
    private void onKeyPressedEnterAddItemToInvoice(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeAddItemToInvoice();
        }
    }

    @FXML
    private void onKeyPressedEnterExitInvoiceManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitInvoiceManagement();
        }
    }


    @FXML
    private void onMouseClickedDeleteArticleFromInvoice(MouseEvent event) {
        executeDeleteArticleFromInvoice();
    }

    @FXML
    private void onMouseClickedDeleteInvoice(MouseEvent event) {
        executeDeleteInvoice();
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
    private void onMouseClickedShowInvoiceItems(MouseEvent event) {
        executeShowInvoiceItems();
    }

    @FXML
    private void onMouseClickedAddItemToInvoice(MouseEvent event) {
        executeAddItemToInvoice();
    }

    @FXML
    private void onMouseClickedExitInvoiceManagement(MouseEvent event) {
        executeExitInvoiceManagement();
    }


    private void setInvoiceItemFieldsEditable(boolean editable) {
        field_invoice_number.setEditable(editable);
        field_customer_number.setEditable(editable);
        field_payment_method.setEditable(editable);
        field_transaction_number.setEditable(editable);
        field_payment_amount.setEditable(editable);
        datePicker_purchase_date.setEditable(editable);
        field_shipping_method.setEditable(editable);
        field_shipping_receipt.setEditable(editable);
        field_shipping_cost.setEditable(editable);
    }


    private void setTableViewVisible(String tableName) {
        if (tableName.equals("invoices")) {
            tableView_invoices.setVisible(true);
            tableView_articles.setVisible(false);
            tableView_customers.setVisible(false);
            tableView_invoice_item.setVisible(false);
        } else if (tableName.equals("invoice_items")) {
            tableView_articles.setVisible(false);
            tableView_customers.setVisible(false);
            tableView_invoices.setVisible(false);
            tableView_invoice_item.setVisible(true);
        } else if (tableName.equals("customers")) {
            tableView_customers.setVisible(true);
            tableView_invoices.setVisible(false);
            tableView_articles.setVisible(false);
            tableView_invoice_item.setVisible(false);
        } else if (tableName.equals("articles")) {
            tableView_articles.setVisible(true);
            tableView_customers.setVisible(false);
            tableView_invoices.setVisible(false);
            tableView_invoice_item.setVisible(false);
        }
    }


    private void clearFields() {
        field_customer_number.clear();
        datePicker_purchase_date.setValue(null);
        field_payment_method.clear();
        field_transaction_number.clear();
        field_payment_amount.clear();
        field_shipping_method.clear();
        field_shipping_receipt.clear();
        field_shipping_cost.clear();
        field_articleID.clear();
        field_amount.clear();
    }

}
