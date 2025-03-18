package com.example.styleslick.controller;

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
import java.util.*;

public class InvoiceManagementController implements Initializable {

    private InvoiceService invoiceService;
    private ArticleService articleService;
    private CustomerService customerService;


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
    private DatePicker datePicker_purchase_date;

    @FXML
    private TextField field_invoice_number;

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
    private TableView<Customer> tableView_customers;

    @FXML
    private TableView<Invoice> tableView_invoices;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceService = InvoiceService.getInstance();
        articleService = ArticleService.getInstance();
        customerService = CustomerService.getInstance();

        executeShowAllInvoices();
    }


    private void executeShowAllCustomers() {
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


    private void executeShowAllInvoices() {
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


        if (datePicker_purchase_date.getValue() == null) {
            AlertService.showErrorAlert("Bestelldatum darf nicht leer sein.");
            return;
        }

        invoiceFields.put("customer_number", field_customer_number.getText());
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


    private void executeDeleteInvoice() {
        if (tableView_invoices.getSelectionModel().getSelectedItem() == null) {
            AlertService.showErrorAlert("Bitte wählen Sie eine Bestellung aus, die Sie löschen möchten");
            executeShowAllInvoices();
            return;
        }

        Invoice selectedInvoice = tableView_invoices.getSelectionModel().getSelectedItem();

        if (AlertService.showConfirmAlertResult("Möchten Sie die Bestellung '" + selectedInvoice.getInvoiceNumber() + "' wirklich löschen? Falls Artikel enthalten sind, wird der Bestand automatisch angepasst.")) {
            if (invoiceService.deleteInvoice(selectedInvoice.getInvoiceID())) {
                executeShowAllInvoices();
            }
        }
    }


    private void executeUpdateInvoice() {

        if (tableView_invoices.getSelectionModel().getSelectedItem() == null) {
            AlertService.showErrorAlert("Bitte wählen Sie die zu bearbeitende Bestellung aus.");
            return;
        }

        Invoice selectedInvoice = tableView_invoices.getSelectionModel().getSelectedItem();
        tableView_invoices.getSelectionModel().clearSelection();
        Map<String, String> invoiceFields = new HashMap<>();


        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich die Bestellung mit der Bestell-Nr: '" +
                selectedInvoice.getInvoiceNumber() + "' bearbeiten?")) {

            AlertService.showConfirmAlert("Die Bestellung wird nicht bearbeite.");
            return;
        }


        if (datePicker_purchase_date.getValue() != null) {
            invoiceFields.put("purchase_date", datePicker_purchase_date.getValue().toString());
        }

        invoiceFields.put("customer_number", field_customer_number.getText());
        invoiceFields.put("invoice_number", field_invoice_number.getText());

        invoiceFields.put("payment_method", field_payment_method.getText());
        invoiceFields.put("transaction_number", field_transaction_number.getText());
        invoiceFields.put("payment_amount", field_payment_amount.getText());
        invoiceFields.put("shipping_method", field_shipping_method.getText());
        invoiceFields.put("shipping_receipt", field_shipping_receipt.getText());
        invoiceFields.put("shipping_cost", field_shipping_cost.getText());

        if (invoiceService.updateInvoice(invoiceFields, selectedInvoice)) {
            clearFields();
            executeShowAllInvoices();
        }

    }


    private void executeExitInvoiceManagement() {
        articleService.clearSession();
        customerService.clearSession();
        invoiceService.clearSession();
        SceneManager.switchScene("/com/example/styleslick/Home-view.fxml", "Willkommen", false);
    }


    @FXML
    private void onKeyPressedEnterUpdateInvoice(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeUpdateInvoice();
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
    private void onKeyPressedEnterExitInvoiceManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitInvoiceManagement();
        }
    }


    @FXML
    private void onMouseClickedUpdateInvoice(MouseEvent event) {
        executeUpdateInvoice();
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
    private void onMouseClickedAddInvoice(MouseEvent event) {
        executeAddInvoice();
    }

    @FXML
    private void onMouseClickedShowInvoiceItems(MouseEvent event) {
        executeShowInvoiceItems();
    }


    @FXML
    private void onMouseClickedExitInvoiceManagement(MouseEvent event) {
        executeExitInvoiceManagement();
    }


    private void setTableViewVisible(String tableName) {
        if (tableName.equals("invoices")) {
            tableView_invoices.setVisible(true);
            tableView_customers.setVisible(false);
        } else if (tableName.equals("invoice_items")) {
            tableView_customers.setVisible(false);
            tableView_invoices.setVisible(false);
        } else if (tableName.equals("customers")) {
            tableView_customers.setVisible(true);
            tableView_invoices.setVisible(false);
        } else if (tableName.equals("articles")) {
            tableView_customers.setVisible(false);
            tableView_invoices.setVisible(false);
        }
    }


    private void clearFields() {
        field_customer_number.clear();
        field_invoice_number.clear();
        datePicker_purchase_date.setValue(null);
        field_payment_method.clear();
        field_transaction_number.clear();
        field_payment_amount.clear();
        field_shipping_method.clear();
        field_shipping_receipt.clear();
        field_shipping_cost.clear();
    }

}
